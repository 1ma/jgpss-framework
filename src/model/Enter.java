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
package model;

import java.util.HashMap;
import java.util.PriorityQueue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

/**
 * A class representing the ENTER block.
 *
 * @author Pau Fonseca i Casas
 * @version 1
 * @see <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@NoArgsConstructor
public class Enter extends Bloc {

    @Getter
    @Setter
    private String A;
    
    @Getter
    @Setter
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

        super(Constants.idDepart, label, comentari);
        this.A = A;
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

        HashMap<String, FacilityState> facilities = this.getModel().getFacilities();
        Bloc _nextBloc = null;

        if (B == 0) {
            B = 1;
        }

        if (facilities.get(A) == null) {

            // The facility has never been used yet and it is available
            // Now we take B free spaces from the capacity 
            FacilityState fs = new FacilityState();
            fs.setMaxCapacity(getModel().getStorageMaxCapacity(A));
            fs.capture(B, tr);
            facilities.put(A, fs);
            _nextBloc = nextBloc(tr);

        } else if (facilities.get(A).capture(B, tr)) {

            // The facility has space for B transactions
            _nextBloc = nextBloc(tr);

        } else {
            // The facility is bussy
            if (this.getModel().getBEC().get(this.A) == null) {
                this.getModel().getBEC().put(this.A, new PriorityQueue<>(1000, this.getModel().getPriorityComparator()));
            }
            this.getModel().getBEC().get(this.A).add(tr);
        }
        return _nextBloc;
    }

    @Override
    public boolean test(Xact tr) {

        HashMap<String, FacilityState> facilities = this.getModel().getFacilities();

        if (facilities.get(A) == null) {
            FacilityState fs = new FacilityState();
            fs.setMaxCapacity(getModel().getStorageMaxCapacity(A));
            facilities.put(A, fs);
        }

        boolean available = facilities.get(A).isAvailable();

        if (!available) {
            tr.setDelay(true);
        }
        return available;
    }
}
