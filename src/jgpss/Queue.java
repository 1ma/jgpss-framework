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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A class representing the QUEUE block.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
public class Queue extends Bloc {

    private String A;
    private int B;

    /**
     * Creates a new instance of Queue
     *
     * @param comentari the comment of the block.
     * @param label the label of the block.
     * @param B the name of the QUEUE.
     * @param A the number of "spaces" to capture.
     */
    public Queue(String comentari, String label, String A, int B) {

        this.setId(Constants.idQueue);
        this.setLabel(label);
        this.setComentari(comentari);
        this.A = A;
        this.B = B;
    }

    /**
     * To get the name of the QUEUE.
     *
     * @return the name of the QUEUE.
     */
    public String getA() {
        return A;
    }

    /**
     * To set the name of the QUEUE.
     *
     * @param A the new name of the QUEUE.
     */
    public void setA(String A) {
        this.A = A;
    }

    /**
     * To get the number of "spaces" to capture.
     *
     * @return the number of "spaces" to capture.
     */
    public int getB() {
        return B;
    }

    /**
     * To set the number of "spaces" to capture.
     *
     * @param B the new number of "spaces" to capture.
     */
    public void setB(int B) {
        this.B = B;
    }

    @Override
    public Bloc execute(Xact tr) {

        HashMap<String, ArrayList> queues = this.getModel().getQueues();

        if (queues.get(this.A) == null) {
            
            ArrayList<Integer> queueStatistics = new ArrayList<Integer>();
            queueStatistics.set(0, 1);
            queueStatistics.set(1, 1);            
            queues.put(this.A, queueStatistics);
        }
        else {
            ArrayList<Integer> queueStatistics = queues.get(this.A);
            Integer current = queueStatistics.get(0) + 1;
            Integer max = queueStatistics.get(1) + 1;
            
            queueStatistics.set(0, current);
            queueStatistics.set(1, max);
        }

        return nextBloc(tr);

    }
}
