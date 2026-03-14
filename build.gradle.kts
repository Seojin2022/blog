plugins {
	java
	id("org.springframework.boot") version "3.5.11"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "me"
version = "0.0.1-SNAPSHOT"
description = "Demo project for Spring Boot"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation ("org.springframework.boot:spring-boot-starter-mail")
	implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	runtimeOnly ("org.postgresql:postgresql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	compileOnly ("org.projectlombok:lombok")
	annotationProcessor ("org.projectlombok:lombok")
}


tasks.withType<Test> {
	useJUnitPlatform()
}
