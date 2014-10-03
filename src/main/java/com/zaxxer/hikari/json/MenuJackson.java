package com.zaxxer.hikari.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.json.obj.MenuBar;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class MenuJackson
{
	private final ObjectMapper mapper = new ObjectMapper();
	private ByteArrayInputStream bais;
	
	@Setup
    public void setup()
    {
		File file = new File("src/test/resources/menu.json");
		byte[] input;
		try (InputStream is = new FileInputStream(file)) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(is, baos);
			baos.close();
			input = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		bais = new ByteArrayInputStream(input);
		bais.mark(0);
    }

	@Benchmark
    public MenuBar inputStream() throws JsonParseException, JsonMappingException, IOException
    {
		bais.reset();
		return mapper.readValue(bais, MenuBar.class);
    }
}
