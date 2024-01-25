package com.nayaragaspar.gprfid;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class GpRfidApplication {

	public static void main(String[] args) {
		SpringApplication.run(GpRfidApplication.class, args);
	}

}
