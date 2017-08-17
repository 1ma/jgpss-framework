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
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

/**
 * A class representing the SEIZE block.
 *
 * @author Pau Fonseca i Casas
 * @author Ezequiel Andujar Montes
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@NoArgsConstructor
public class Seize extends Bloc {

    @Getter
    @Setter
    private String A;

    @Getter
    @Setter
    private int captureCount;

    /**
     * Creates a new instance of Seize
     *
     * @param comentari the comment of the block.
     * @param label the label of the block.
     * @param A the name of the SEIZE.
     */
    public Seize(String comentari, String label, String A) {

        super(Constants.idSeize, label, comentari);
        this.captureCount = 0;
        this.A = A;
    }

    /**
     * Increments the number of times a server has been capture
     */
    public void incCaptureCount() {
        captureCount++;
    }

    @Override
    public Bloc execute(Xact tr) {
        HashMap<String, FacilityState> facilities = this.getModel().getFacilities();

        Bloc nextBloc;
        if (facilities.get(this.A) == null) {
            FacilityState fs = new FacilityState();
            fs.capture(tr);
            facilities.put(this.A, fs);
            nextBloc = nextBloc(tr);

        } // Attempt to capture the seize
        else if (facilities.get(A).capture(tr)) {
            nextBloc = nextBloc(tr);
        } // The facility is bussy
        else {
            this.getModel().getCEC().remove(tr);
            if (this.getModel().getBEC().get(this.A) == null) {
                this.getModel().getBEC().put(this.A, new PriorityQueue<>(1000, this.getModel().getPriorityComparator()));
            }
            this.getModel().getBEC().get(this.A).add(tr);
            nextBloc = null;
        }
        return nextBloc;
    }

    @Override
    public boolean test(Xact tr) {

        HashMap<String, FacilityState> facilities = this.getModel().getFacilities();

        if (facilities.get(A) == null) {
            FacilityState fs = new FacilityState();
            facilities.put(A, fs);
        }

        boolean available = facilities.get(A).isAvailable();

        if (!available) {
            tr.setDelay(true);
        }
        return available;
    }
}
