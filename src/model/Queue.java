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

import java.util.HashMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

/**
 * A class representing the QUEUE block.
 *
 * @author Pau Fonseca i Casas
 * @author Ezequiel Andujar Montes
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@NoArgsConstructor
@SuppressWarnings("FieldMayBeFinal")
public class Queue extends Bloc {

    @Getter
    @Setter
    private String A;

    @Getter
    @Setter
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

        super(Constants.idQueue, label, comentari);
        this.A = A;
        this.B = B;
    }

    @Override
    public Bloc execute(Xact tr) {

        incTrans(tr);
        HashMap<String, QueueReport> queues = this.getModel().getQueues();

        int increment = 1;

        if (this.B > 0) {
            increment = this.B;
        }

        if (queues.get(this.A) == null) {
            QueueReport queueStatistics = new QueueReport();
            queues.put(this.A, queueStatistics);
        }
        QueueReport queueStatistics = queues.get(this.A);
        queueStatistics.incCurrentCount(increment);
        queueStatistics.incTotalEntries(increment);

        return nextBloc(tr);
    }   
}
