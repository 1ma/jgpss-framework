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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import lombok.Cleanup;
import model.interfaces.Report;

/**
 *
 * @author Ezequiel Andujar Montes
 */
public class TxtReport implements Report {

    private final Model model;

    public TxtReport(Model model) {
        this.model = model;
    }

    @Override
    public void createReport() throws FileNotFoundException {

        File file = new File("resources/reports/" + model.getNomModel() + ".txt");

        @Cleanup
        PrintWriter writer = new PrintWriter(file);

        // Header Information
        writer.println("JGPSS model report" + " - " + model.getNomModel());
        writer.println(new Timestamp(System.currentTimeMillis()) + "\n");

        // General Information
        int totalBlocks = model.getProces().stream().mapToInt(p -> p.getBlocs().size()).sum();
        writer.println(String.format("%12s %12s %10s %15s %10s", "START TIME", "END TIME", "BLOCKS", "FACILITIES", "STORAGES"));
        writer.println(String.format("%12f %12f %10d %15d %10d", 0.000f, model.getAbsoluteClock(), totalBlocks, model.getFacilities().size(), model.getStorages().size()));

        // Block Information
        model.getProces().forEach(p -> {

            writer.println("PROCESS: " + p.getDescpro());
            writer.println(String.format("%10s %5s %10s %10s %10s", "LABEL", "LOC", "BLOCK TYPE", "ENTRY COUNT", "RETRY"));

            p.getBlocs().forEach(b  -> {

                writer.println(String.format("%10s %5d %10s %10d %10d", b.getLabel(), b.getPos(), b.getClass().getName(), b.getXactCounter(), "RETRY"));

            });

            writer.println();

        });
    }
}
