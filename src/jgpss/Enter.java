/**
 * Software end-user license agreement.
 *
 * The LICENSE.TXT containing the license is located in the JGPSS project.
 * License.txt can be downloaded here:
 * href="http://www-eio.upc.es/~Pau/index.php?q=node/28
 *
 * NOTICE TO THE USER: BY COPYING, INSTALLING OR USING THIS SOFTWARE OR PART OF
 * THIS SOFTWARE, YOU AGREE TO THE TERMS AND CONDITIONS OF THE LICENSE AGREEMENT
 * AS IF IT WERE A WRITTEN AGREEMENT NEGOTIATED AND SIGNED BY YOU. THE LICENSE
 * AGREEMENT IS ENFORCEABLE AGAINST YOU AND ANY OTHER LEGAL PERSON ACTING ON
 * YOUR BEHALF. IF, AFTER READING THE TERMS AND CONDITIONS HEREIN, YOU DO NOT
 * AGREE TO THEM, YOU MAY NOT INSTALL THIS SOFTWARE ON YOUR COMPUTER. UPC IS THE
 * OWNER OF ALL THE INTELLECTUAL PROPERTY OF THE SOFTWARE AND ONLY AUTHORIZES
 * YOU TO USE THE SOFTWARE IN ACCORDANCE WITH THE TERMS SET OUT IN THE LICENSE
 * AGREEMENT.
 */
package jgpss;

import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

/**
 * A class representing the ENTER block.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
public class Enter extends Bloc {

    private String A;
    private int B;

    /**
     * Creates a new instance of Enter
     *
     * @param comentari the comment of the block.
     * @param label the block label.
     * @param A the name of the storage.
     * @param B the number of instances needed.
     */
    public Enter(String comentari, String label, String A, int B) {

        this.setId(Constants.idEnter);
        this.setLabel(label);
        this.setComentari(comentari);
        this.A = A;
        this.B = B;
    }

    /**
     * To obtain the name of the storage
     *
     * @return the name of the storage.
     */
    public String getA() {
        return A;
    }

    /**
     * To set the name of the storage.
     *
     * @param A the new name of the storage to catch.
     */
    public void setA(String A) {
        this.A = A;
    }

    /**
     * To obtain the number of instances needed.
     *
     * @return the number of instance needed.
     */
    public int getB() {
        return B;
    }

    /**
     * Set method for the number of instances.
     *
     * @param B the number of instances needed.
     */
    public void setB(int B) {
        this.B = B;
    }

    /**
     * To execute the block
     *
     * @param tr the current XACT (that cross the block).
     * @return return NULL if no more blocs can be executed now. The next block
     * otherwise.
     */
    @Override
    public Bloc execute(Xact tr) {
        HashMap<String, Integer> facilities = this.getModel().getFacilities();
        if (facilities.get(this.A) == null) {
            // La facility encara no s'ha utilitzat mai i esta lliure
            facilities.put(this.A, 1);
            return nextBloc(tr);
        } else if (facilities.get(this.A) < this.B) {
            // La facility esta lliure
            facilities.put(this.A, facilities.get(this.A) + this.B);
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
