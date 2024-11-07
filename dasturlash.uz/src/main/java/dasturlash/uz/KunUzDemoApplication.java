package dasturlash.uz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import java.util.ArrayList;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;


@SpringBootApplication
//@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class KunUzDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KunUzDemoApplication.class, args);
        System.out.println("Running...");

        ArrayList<Integer> arrays = new ArrayList<>(3);

        arrays.add(1);
        arrays.add(1);
        arrays.add(1);
        arrays.add(1);

        System.out.println(arrays);
    }

}
