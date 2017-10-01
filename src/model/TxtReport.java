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
    public void createReport() throws Exception {

        File file = new File("resources/reports/" + model.getNomModel() + ".txt");

        @Cleanup
        PrintWriter writer = new PrintWriter(file);

        printGeneralInfo(writer);
        printBlockInfo(writer);
        printFacilityInfo(writer);

    }

    private void printGeneralInfo(PrintWriter writer) {
        
        writer.println("JGPSS model report" + " - " + model.getNomModel());
        writer.println(new Timestamp(System.currentTimeMillis()) + "\n");

        int totalBlocks = model.getProces().stream().mapToInt(p -> p.getBlocs().size()).sum();
        writer.println(String.format("%-12s %-12s %-10s %-15s %-10s", "START TIME", "END TIME", "BLOCKS", "FACILITIES", "STORAGES"));
        writer.println(String.format("%-12f %-12f %-10d %-15d %-10d", 0.000f, model.getRelativeClock(), totalBlocks, model.getFacilities().size(), model.getStorages().size()));
        writer.println("\n");
    }

    private void printBlockInfo(PrintWriter writer) {

        model.getProces().forEach(p -> {

            writer.println("PROCESS: " + p.getDescpro() + "\n");
            writer.println(String.format("%-12s %-6s %-12s %-14s %-10s %-10s", "LABEL", "LOC", "BLOCK TYPE", "ENTRY COUNT", "CURRENT COUNT", "RETRY"));

            p.getBlocs().forEach(b -> {

                writer.println(String.format("%-12s %-6d %-12s %-14d %-14d %-10d", b.getLabel(), b.getPos(), b.getClass().getName().split("model.")[1], b.getEntryCount(), b.getCurrentCount(), b.getRetryCount()));

            });
            writer.println();
        });
        writer.println();
    }

    private void printFacilityInfo(PrintWriter writer) {

        model.getFacilities().forEach((key, value) -> {

            Facility facility = value;
            String fn = key;

            writer.println(String.format("%-12s%-12s%-10s%-15s%-10s%-10s%-10s%-10s",
                    "FACILITY", "ENTRIES", "UTIL.", "AVE. TIME", "AVAIL.", "OWNER", "INTER", "DELAY"));

            String facilityName = fn;
            int facilityCounter = facility.getCounterCount();
            float utilizationTime = model.getRelativeClock() != 0 ? facility.getUtilizationTime() / model.getRelativeClock() : 0;
            float avgTime = facility.avgHoldingTime();
            int available = facility.isAvailable() ? 1 : 0;
            String ownXactID = facility.getOwningXact() != null ? String.valueOf(facility.getOwningXact().getID()) : "-";
            int premptXacts = model.getPreemptedXacts().get(fn) != null ? model.getPreemptedXacts().get(fn).size() : 0;
            int blockedXacts = model.getBEC().get(fn) != null ? model.getBEC().get(fn).size() : 0;

            String f = String.format("%-12s%-12d%-10f%-15f%-10d%-10s%-10d%-10d",
                    fn, facilityCounter, utilizationTime, avgTime, available,
                    ownXactID, premptXacts, blockedXacts);

            writer.println(f);
        });
    }
}
