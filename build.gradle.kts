import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

plugins {
	id("com.google.protobuf") version "0.8.16"
	id("me.champeau.gradle.jmh") version "0.5.3"
	id("idea")
	id("java")
	kotlin("jvm") version "1.5.20"
	kotlin("kapt") version "1.5.20"
}

group = "com.architype"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.fasterxml.jackson.core:jackson-core:2.12.3")
	implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.12.3")
	implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.12.3")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("io.grpc:grpc-netty-shaded:1.38.0")
	implementation("io.grpc:grpc-protobuf:1.38.0")
	implementation("io.grpc:grpc-stub:1.38.0")
	implementation("org.openjdk.jmh:jmh-core:1.25")
	implementation("com.ibm.icu:icu4j:69.1")

	implementation("ma.glasnost.orika:orika-core:1.5.4")
	implementation("org.mapstruct:mapstruct:1.4.2.Final")
	implementation("org.openjdk.jmh:jmh-generator-annprocess:1.25")
	// https://stackoverflow.com/questions/45085806/kotlins-kapt-plugin-for-gradle-does-not-work-for-custom-source-set-jmh
	kapt("org.mapstruct:mapstruct-processor:1.4.2.Final")
	kaptJmh("org.openjdk.jmh:jmh-generator-annprocess:1.25")

	implementation("com.google.protobuf:protobuf-java-util:3.17.3")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:3.17.3"
	}

	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:1.38.1"
		}
	}

	generateProtoTasks {
		all().forEach {
			it.plugins {
				id("grpc")
			}
		}
	}
}

idea {
	module {
		listOf("java", "grpc").forEach { dir ->
			sourceSets.getByName("main").java { srcDir("$buildDir/generated/source/proto/main/$dir") }
		}
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
