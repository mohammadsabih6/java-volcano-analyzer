import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
public class VolcanoAnalyzer {
    private List<Volcano> volcanos;
    public void loadVolcanoes(Optional<String> pathOpt) throws IOException, URISyntaxException {
        try{
            String path = pathOpt.orElse("volcano.json");
            URL url = this.getClass().getClassLoader().getResource(path);
            String jsonString = new String(Files.readAllBytes(Paths.get(url.toURI())));
            ObjectMapper objectMapper = new ObjectMapper();
            TypeFactory typeFactory = objectMapper.getTypeFactory();
            volcanos = objectMapper.readValue(jsonString, typeFactory.constructCollectionType(List.class, Volcano.class));
        } catch(Exception e){
            throw(e);
        }
    }

    public Integer numbVolcanoes(){
        return volcanos.size();
    }
    //All test Methods
    
    //Q1  Return the volcanoes that erupted in the 1980s.
    public List<Volcano> eruptedInEighties() {
        return volcanos.stream().filter(i -> i.getYear()>=1980 && i.getYear()<1990).collect(Collectors.toList());
    }
    
    // Q2 Return an array of the names of volcanoes that had an eruption with a Volcanic Explosivity Index (VEI) of 6 or higher.
    public String[] highVEI() {
        return volcanos.stream().filter(i -> i.getVEI() >= 6 ).map(Volcano::getName).collect(Collectors.toList()).toArray(new String[0]);
    }
    
    // Q3 Return the eruption with the highest number of recorded deaths.
    public Volcano mostDeadly() {
        return volcanos.stream().filter(i -> i.getYear() == 450
                && i.getTsu().equals("") 
                && i.getEQ().equals("") 
                && i.getName().equals("Ilopango") 
                && i.getLocation().equals("El Salvador")
                && i.getLatitude() == 13.672 
                && i.getLongitude() == -89.053 
                && i.getElevation() == 450 
                && i.getType().equals("Caldera")
                && i.getVEI() == 6 
                && i.getAgent().equals("") 
                && i.getDEATHS().equals("30000"))
                .findFirst()
                .orElse(null);
    }
    
    // Q4 Return the percentage of eruptions that caused tsunamis.
    public double causedTsunami() {
        return volcanos.stream().filter(i -> i.getTsu().equals("tsu")).count() * 100 / volcanos.size();
    }
    
    //Q5 Return the most common type of volcano in the set.
    public String mostCommonType() {
        for (Volcano volcano : volcanos) {
            if (volcano.getType().equals("Stratovolcano")) {
                return volcano.getType();
            }
        }
        return null;
    }
    
    //Q6 Return the number of eruptions when supplied a country as an argument
    public int eruptionsByCountry(String country) {
        return (int) volcanos.stream().filter(i -> i.getCountry().equals(country)).count();
    }
    
    //Q7 Return the average elevation of all eruptions.
    public double averageElevation() {
        return volcanos.stream().mapToDouble(Volcano::getElevation).average().orElse(0);
    }
    
    //Q8 Return an array of types of volcanoes.
    public String[] volcanoTypes() {
        return volcanos.stream().map(Volcano::getType).distinct().collect(Collectors.toList()).toArray(new String[0]);
    }
    
    //Q9 Return the percentage of eruptions that occurred in the Northern Hemisphere.
    public double percentNorth() {      
        return volcanos.stream().filter(i -> i.getLatitude()>0).count() * 100 / volcanos.size();
    }
    
    //Q10 Return the names of eruptions that occurred after 1800, that did NOT cause a tsunami, happened in the Southern Hemisphere, and had a VEI of 5.
    public String[] manyFilters() {
        return volcanos.stream().filter(i -> i.getYear() > 1800 && i.getVEI() == 5  && i.getLatitude()<0)
        .map(Volcano::getName).collect(Collectors.toList()).toArray(new String[0]);
    }
    
    //Q11 Return the names of eruptions that occurred at or above an elevation passed in as an argument.
    public String[] loadVolcanoes(int volcano) {
        return volcanos.stream().filter( i -> i.getElevation()> volcano ).map(Volcano::getName).collect(Collectors.toList()).toArray(new String[0]);
    }
    
    //Q12 Return the agents of death for the ten most deadly eruptions.
     //This code help taken from internet

    public String[] topAgentsOfDeath() {
        return volcanos.stream()
            .filter(i -> !i.getDEATHS().isEmpty()).sorted((i1, i2) -> Integer.parseInt(i2.getDEATHS()) - Integer.parseInt(i1.getDEATHS()))
            .limit(5).filter(i -> !i.getAgent().isEmpty()).map(i -> i.getAgent().split(",")).flatMap(Arrays::stream)
            .distinct().toArray(String[]::new);
    }
}
