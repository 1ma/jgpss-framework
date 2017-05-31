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
package jgpss;

import java.util.HashMap;
import java.util.PriorityQueue;
import javax.annotation.Resources;

/**
 * A class representing the SEIZE block.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
public class Seize extends Bloc {

    private String A;

    /**
     * Creates a new instance of Seize
     *
     * @param comentari the comment of the block.
     * @param label the label of the block.
     * @param A the name of the SEIZE.
     */
    public Seize(String comentari, String label, String A) {

        this.setId(Constants.idSeize);
        this.setLabel(label);
        this.setComentari(comentari);
        //   this.id_model= id_model;
        this.A = A;
    }

    /**
     * To obtain the name of the SEIZE.
     *
     * @return the name.
     */
    public String getA() {
        return A;
    }

    /**
     * To modify the name of the SEIZE.
     *
     * @param A the name.
     */
    public void setA(String A) {
        this.A = A;
    }

    @Override
    public Bloc execute(Xact tr) {
        HashMap<String, Integer> facilities = this.getModel().getFacilities();
        if (facilities.get(this.A) == null || facilities.get(this.A) == 0) {
            facilities.put(this.A, 1);
            return nextBloc(tr);
        } else {
            // La facility esta ocupada
            this.getModel().getCEC().remove(tr);
            if (this.getModel().getBEC().get(this.A) == null) {
                this.getModel().getBEC().put(this.A, new PriorityQueue<Xact>(1000, this.getModel().getPriorityComparator()));
            }
            this.getModel().getBEC().get(this.A).add(tr);
            return null;
        }
    }
}
