package domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Friendship extends Entity<Tuple<Long,Long>>{
    LocalDateTime date;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy:MM:dd HH:mm:ss");
    public Friendship(Long id1, Long id2){
        Tuple<Long,Long> id = new Tuple<>(id1, id2);
        setId(id);
        this.date=LocalDateTime.now();
    }

    /**
     * @return the date when the friendship was created
     */
    public LocalDateTime getDate(){return date;}
}
