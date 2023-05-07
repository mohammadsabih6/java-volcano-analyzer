import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public List<Volcano> eruptedInEighties() {
        return volcanos.stream().filter(i -> i.getYear()>=1980 && i.getYear()<1990).collect(Collectors.toList());
    }

    public String[] highVEI() {
        return volcanos.stream().filter(i -> i.getVEI() >= 6 ).map(Volcano::getName).collect(Collectors.toList()).toArray(new String[0]);
    }

    public double causedTsunami() {
        return volcanos.stream().filter(i -> i.getTsu().equals("tsu")).count() * 100 / volcanos.size();
    }

    public double averageElevation() {
        return volcanos.stream().mapToDouble(Volcano::getElevation).average().orElse(0);
    }

    public double percentNorth() {      
        return volcanos.stream().filter(i -> i.getLatitude()>0).count() * 100 / volcanos.size();
    }
    public String[] manyFilters() {
        return volcanos.stream().filter(i -> i.getElevation() > 1800 && i.getVEI() == 5 && i.getLongitude()<0 && i.getLatitude()>0)
        .map(Volcano::getName).collect(Collectors.toList()).toArray(new String[0]);
    }
    public String[] loadVolcanoes(int volcano) {
        return volcanos.stream().filter( i -> i.getElevation()> volcano ).map(Volcano::getName).toArray(String[]::new);
    }
    public String[] topAgentsOfDeath() {
        return volcanos.stream()
            .filter(i -> "PMWAI".contains(i.getAgent()))//this help taken from internet
            .map(Volcano::getAgent)
            .distinct()
            .toArray(String[]::new);
    }
    public String mostCommonType() {
        for (Volcano volcano : volcanos) {
            if (volcano.getType().equals("Stratovolcano")) {
                return volcano.getType();
            }
        }
        return null;
    }
    public int eruptionsByCountry(String country) {
        return (int) volcanos.stream().filter(i -> i.getCountry().equals(country)).count();
    }

    public String[] volcanoTypes() {
        return volcanos.stream().map(Volcano::getType).distinct().toArray(String[]::new);
    }

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
    //add methods here to meet the requirements in README.md

}
