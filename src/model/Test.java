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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.Constants;

/**
 * A class representing the TEST block.
 *
 * @author Pau Fonseca i Casas
 * @author Ezequiel Andujar Montes
 * @version 1
 * @see     <a href="http://www-eio.upc.es/~Pau/index.php?q=node/28">Project
 * website</a>
 * @serialData
 */
@NoArgsConstructor
public class Test extends Bloc {

    //Relation operator
    @Getter
    @Setter
    private String x;
    //Parameters.
    @Getter
    @Setter
    private String A;

    @Getter
    @Setter
    private String B;

    @Getter
    @Setter
    private String C;
    /**
     * String to identify the E test.
     */
    public static final String E = "E";
    /**
     * String to identify the G test.
     */
    public static final String G = "G";
    /**
     * String to identify the GE test.
     */
    public static final String GE = "GE";
    /**
     * String to identify the L test.
     */
    public static final String L = "L";
    /**
     * String to identify the LE test.
     */
    public static final String LE = "LE";
    /**
     * String to identify the NE test.
     */
    public static final String NE = "NE";

    /**
     * Creates a new instance of Test.
     *
     * @param comentari the comment of the block.
     * @param label the label of the block.
     * @param x the type of TEST.
     * @param A the value to compare.
     * @param B the reference.
     * @param C te label to jump if false.
     */
    public Test(String comentari, String label, String x, String A, String B, String C) {

        super(Constants.idTest, label, comentari);
        this.A = A;
        this.B = B;
        this.C = C;
        this.x = x;
    }

    @Override
    public Bloc execute(Xact tr) {

        incTrans(tr);

        Float _A = Float.parseFloat(getModel().evaluateExpression(this.A, tr));
        Float _B = Float.parseFloat(getModel().evaluateExpression(this.B, tr));

        Bloc nextBloc;

        if (!this.C.isEmpty()) {
            nextBloc = getModel().findBloc(this.C);
        } else {
            nextBloc = nextBloc(tr);
        }

        boolean test = this.x.equals(E) && _A.equals(_B)
                || this.x.equals(G) && _A > _B
                || this.x.equals(GE) && _A >= _B
                || this.x.equals(L) && _A < _B
                || this.x.equals(LE) && _A <= _B
                || this.x.equals(NE) && !_A.equals(_B);

        if (!test) {
            nextBloc = null;
        }
        return nextBloc;
    }    
}
