import ca.uhn.fhir.rest.client.api.IGenericClient;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ClientResorce {

    static void createFhirClient(IGenericClient client, List<Bundle.BundleEntryComponent> allEntry) {

        //List<Bundle.BundleEntryComponent> allEntry = response.getEntry().stream().collect(Collectors.toList());


        for (int i=0 ; i < allEntry.size(); i++) {
            System.out.println("Int i " + i);

            String StoreId = allEntry.get(i).getResource().getId();
            // Read a Patient
            Patient patient = client.read().resource(Patient.class).withId(StoreId).execute();

            if (patient.getName().get(0).getGiven() != null) {
                System.out.println("name :" + patient.getName().get(0).getGiven());
            }

            if (patient.getBirthDateElement() != null) {
                System.out.println("birth date :" + patient.getBirthDateElement());
            }

            if (patient.getName().get(0).getFamily() != null) {
                System.out.println("family name : " + patient.getName().get(0).getFamily());
            }

        }


    }



    static void readPatientFile(IGenericClient client) {
        Bundle respfile=null;
        String readPatientFile = "readPatientFile";

            

        //spring.cache.type=NONE
        //StopWatch stopWatch = new StopWatch();
        //stopWatch.startTask(readPatientFile);

        long startTime = System.currentTimeMillis();

        //long finish = System.currentTimeMillis();
        //long timeElapsed = finish - start;


        try {
            ///
            File myObj = new File("patient.txt");
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();

                System.out.println("DAta :" + data);

                respfile = client
                        .search()
                        .forResource("Patient")
                        .where(Patient.FAMILY.matches().value(data))
                        .returnBundle(Bundle.class)
                        .execute();

                List<Bundle.BundleEntryComponent> patientEntry = respfile.getEntry()
                        .stream()
                        .collect(Collectors.toList());

                if (! patientEntry.isEmpty()) {

                    createPatientEntry(patientEntry, client);


                }

            }
            myReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }

        long finishTime = System.currentTimeMillis();
        long timeElapsed = finishTime - startTime;
        long averageTime = 0;
        System.out.println("Start time :" + startTime);
        System.out.println("Finish Time :" + finishTime);
        System.out.println("Elapsed Time :" + timeElapsed);

        averageTime = timeElapsed / 20;
        System.out.println("Average Time :" + averageTime);

    }


    private static void createPatientEntry(List<Bundle.BundleEntryComponent> patientEntry, IGenericClient client ) {

        String StoreId = patientEntry.get(0).getResource().getId();
        // Read a Patient
        Patient patient = client.read().resource(Patient.class).withId(StoreId).execute();

        if (patient.getName().get(0).getGiven() != null) {
            System.out.println("name :" + patient.getName().get(0).getGiven());
        }

        if (patient.getBirthDateElement() != null) {
            System.out.println("birth date :" + patient.getBirthDateElement());
        }

        if (patient.getName().get(0).getFamily() != null) {
            System.out.println("family name : " + patient.getName().get(0).getFamily());
        }



    }






}
