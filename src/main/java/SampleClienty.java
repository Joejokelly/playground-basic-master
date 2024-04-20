import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SampleClienty {

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

       Bundle response = client.search()
                .forResource(Patient.class)
                .count(20)
                .returnBundle(Bundle.class)
                .execute();

        //List<Bundle.BundleEntryComponent> allEntry = response.getEntry().stream().collect(Collectors.toList());

        List<Bundle.BundleEntryComponent> allEntry = response.getEntry()
                .stream()
                .collect(Collectors.toList());

        //allEntry.sort();



/*
        List<Bundle.BundleEntryComponent> allSorted = Collections.sort(allEntry, new Comparator<Bundle.BundleEntryComponent>() {
                    @Override
                    public int compare(Bundle.BundleEntryComponent p1, Bundle.BundleEntryComponent p2) {

                        Patient patient = client.read().resource(Patient.class).withId(StoreId).execute();


                        return p1.
                        return 0;
                    }
                }
*/

       // System.out.println("Size :" + allEntry.size());

        // Load the next page
//        org.hl7.fhir.r4.model.Bundle nextPage = client
//                .loadPage()
//                .next(response)
//                .execute();



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


    //Part - ii


        // Note:  Double backquote is to avoid compiler
        // interpret words
        // like \test as \t (ie. as a escape sequence)

        // Creating an object of BufferedReader class
        //String[] sa = new String[20];

        String[] sa=null;

        try {
            ///
            File myObj = new File("patient.txt");
            Scanner myReader = new Scanner(myObj);
            sa = new String[20];
            int i = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                //i++;

                sa[i] = data;
                i++;

                System.out.println(data);
            }
            myReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }

        /*for (int j=0; j < sa.length; j++) {

            System.out.println("sa :" + sa[j]);
        }
*/

        Bundle respfile = null;

        for (int j=0; j < sa.length;  j++) {

            if (sa[j] != null) {
                respfile = client
                        .search()
                        .forResource("Patient")
                        .where(Patient.FAMILY.matches().value(sa[j]))
                        .returnBundle(Bundle.class)
                        .execute();

            List<Bundle.BundleEntryComponent> patientEntry = respfile.getEntry()
                    .stream()
                    .collect(Collectors.toList());

            if (patientEntry.isEmpty()) {
                System.out.println("Patient not found");
            }
            else {

                //System.out.println(patientEntry);

                for (int i=0 ; i < patientEntry.size(); i++) {
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

                } //for


            } //else


        } //if


        } //for



    } //main

} //class
