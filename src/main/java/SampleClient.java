import ca.uhn.fhir.context.FhirContext;

import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.impl.GenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;
//import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class SampleClient {
    static List<Bundle.BundleEntryComponent> allEntry=null;
    //static GenericClient client = null;

    public static void main(String[] theArgs) {

        // Create a FHIR client
        FhirContext fhirContext = FhirContext.forR4();
        IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
        client.registerInterceptor(new LoggingInterceptor(false));

        Bundle response = client.search()
                .forResource(Patient.class)
                .count(10)
                .where(Patient.FAMILY.matches().value("SMITH"))
                .returnBundle(Bundle.class)
                .execute();

        allEntry = response.getEntry()
                .stream()
                .collect(Collectors.toList());

        ClientResorce resource = new ClientResorce();

        resource.createFhirClient(client, allEntry);

    //Part -II
        System.out.println("First run ");
        resource.readPatientFile(client);

        System.out.println("Second run");
        resource.readPatientFile(client);

        System.out.println("Third run, disable cache.");
        resource.readPatientFile(client);


    } //main

    //@Override
   /* protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
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

} //class
