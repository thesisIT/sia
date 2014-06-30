package org.sysreg.sia.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.ModelMap;
import org.sysreg.sia.model.Field;
import org.sysreg.sia.model.User;
import org.sysreg.sia.model.dao.FieldDAO;
import org.sysreg.sia.model.dao.UserDAO;

import java.security.Principal;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by jose on 30/06/14.
 */
public class FieldsControllerTest {

    //Class with autowired objects
    @InjectMocks
    private FieldsController fieldsController = new FieldsController();

    //Services mocked
    @Mock
    private UserDAO userDAO;
    @Mock
    private FieldDAO fieldDAO;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void showFields() {
        //Create the test data
        Principal testUser = new Principal() {
            @Override
            public String getName() {
                return "Test User";
            }
        };
        User expectedUser = new User();
        expectedUser.setName(testUser.getName());

        Field field1 = new Field();
        field1.setUser(expectedUser);

        Field field2 = new Field();
        field2.setUser(expectedUser);

        List<Field> expectedFields = asList(field1, field2);

        //Mock the services behaviour with mockito
        when(userDAO.findByUsername(testUser.getName())).thenReturn(expectedUser);
        when(fieldDAO.findByUser(expectedUser)).thenReturn(expectedFields);

        //Call the controller
        ModelMap model = new ModelMap();
        String viewName = fieldsController.showFieldsPage(model, testUser);

        //Check the controller results
        assertEquals("fields", viewName);
        assertSame(expectedFields, model.get("fields"));

        //Verify that the services was called at least one time
        verify(userDAO, atLeastOnce()).findByUsername(testUser.getName());
        verify(fieldDAO, atLeastOnce()).findByUser(expectedUser);
    }
}
