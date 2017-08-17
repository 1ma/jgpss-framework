/**
 * Software end-user license agreement.
 *
 * The LICENSE.TXT containing the license is located in the JGPSS project.
 * License.txt can be downloaded here:
 * href="http://www-eio.upc.es/~Pau/index.php?q=node/28
 *
 * NOTICE TO THE USER: BY COPYING, INSTALLING OR USING THIS SOFTWARE OR PART OF
 * THIS SOFTWARE, YOU AGREE TO THE   TERMS AND CONDITIONS OF THE LICENSE AGREEMENT
 * AS IF IT WERE A WRITTEN AGREEMENT NEGOTIATED AND SIGNED BY YOU. THE LICENSE
 * AGREEMENT IS ENFORCEABLE AGAINST YOU AND ANY OTHER LEGAL PERSON ACTING ON YOUR
 * BEHALF.
 * IF, AFTER READING THE TERMS AND CONDITIONS HEREIN, YOU DO NOT AGREE TO THEM,
 * YOU MAY NOT INSTALL THIS SOFTWARE ON YOUR COMPUTER.
 * UPC IS THE OWNER OF ALL THE INTELLECTUAL PROPERTY OF THE SOFTWARE AND ONLY
 * AUTHORIZES YOU TO USE THE SOFTWARE IN ACCORDANCE WITH THE TERMS SET OUT IN
 * THE LICENSE AGREEMENT.
 */
package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Ezequiel Andujar Montes
 */
public class GenerateTest {

    private Model model;
    Terminate terminate;
    Generate generate;
    private int TC;

    public GenerateTest() {

        model = new Model();
    }

    @Before
    public void setUp() {

        TC = 20;        
        model.setTC(TC);        
        Proces proces = new Proces("p1", model);   
        
        terminate = new Terminate("", "term", 1);
        generate = new Generate("", "gen", 1f, 2f, 0f, 0f, 0f, 0);        
        proces.getBlocs().add(generate);
        proces.getBlocs().add(terminate);        
        model.getProces().add(proces);

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of execute method, of class Model.
     */
    @Test
    public void testExecute() throws Exception {
        
        
        assertEquals("TC must be 0", model.getTC(), 0);           
        assertEquals("Total xacts count in Terminate must be 20", terminate.getXactCounter(), TC);        
        
    }

}
