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

public class SampleClient_back {
    static List<Bundle.BundleEntryComponent> allEntry=null;
    //static GenericClient client = null;

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

        allEntry = response.getEntry()
                .stream()
                .collect(Collectors.toList());

        createFhirClient(client);

    //Part -II

        readPatientFile(client);



    } //main

    private static void readPatientFile(IGenericClient client) {
        Bundle respfile=null;

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
            myReader.close();
        } catch (FileNotFoundException ex) {
            System.out.println("An error occurred.");
            ex.printStackTrace();
        }



    }


    //@Override

/*
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        String name = createInvocationTraceName(invocation);
        StopWatch stopWatch = new StopWatch(name);
        stopWatch.start(name);
        try {
            return invocation.proceed();
        }
        finally {
            stopWatch.stop();
            writeToLog(logger, stopWatch.shortSummary());
        }
    }

*/

    private static void createFhirClient(IGenericClient client) {


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

} //class
