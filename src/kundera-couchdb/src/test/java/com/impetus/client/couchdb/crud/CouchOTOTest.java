/*******************************************************************************
 * * Copyright 2013 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.client.couchdb.crud;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.impetus.client.couchdb.entities.AddressCouchOTO;
import com.impetus.client.couchdb.entities.PersonCouchOTO;

public class CouchOTOTest
{
    private EntityManagerFactory emf;

    private EntityManager em;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        emf = Persistence.createEntityManagerFactory("couchdb_pu");
        em = getNewEM();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        em.close();
        emf.close();
    }

    @Test
    public void testCRUD()
    {
        AddressCouchOTO address = new AddressCouchOTO();
        address.setAddressId("a");
        address.setStreet("sector 11");

        PersonCouchOTO person = new PersonCouchOTO();
        person.setPersonId("1");
        person.setPersonName("Kuldeep");
        person.setAddress(address);

        em.persist(person);

        em = getNewEM();

        PersonCouchOTO foundPerson = em.find(PersonCouchOTO.class, 1);
        Assert.assertNotNull(foundPerson);
        Assert.assertNotNull(foundPerson.getAddress());
        Assert.assertEquals("1", foundPerson.getPersonId());
        Assert.assertEquals("Kuldeep", foundPerson.getPersonName());
        Assert.assertEquals("a", foundPerson.getAddress().getAddressId());
        Assert.assertEquals("sector 11", foundPerson.getAddress().getStreet());

        foundPerson.setPersonName("KK");
        foundPerson.getAddress().setStreet("sector 12");

        em.merge(foundPerson);

        em = getNewEM();

        foundPerson = em.find(PersonCouchOTO.class, 1);
        Assert.assertNotNull(foundPerson);
        Assert.assertNotNull(foundPerson.getAddress());
        Assert.assertEquals("1", foundPerson.getPersonId());
        Assert.assertEquals("KK", foundPerson.getPersonName());
        Assert.assertEquals("a", foundPerson.getAddress().getAddressId());
        Assert.assertEquals("sector 12", foundPerson.getAddress().getStreet());

        em.remove(foundPerson);
        foundPerson = em.find(PersonCouchOTO.class, 1);
        Assert.assertNull(foundPerson);
    }

    private EntityManager getNewEM()
    {
        if (em != null && em.isOpen())
        {
            em.close();
        }
        return em = emf.createEntityManager();
    }
}