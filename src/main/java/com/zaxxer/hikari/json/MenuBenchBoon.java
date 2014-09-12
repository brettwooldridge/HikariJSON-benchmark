package com.zaxxer.hikari.json;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.boon.json.JsonParserAndMapper;
import org.boon.json.JsonParserFactory;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import com.zaxxer.hikari.json.obj.MenuBar;


@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
public class MenuBenchBoon
{
	private byte[] input;
	private final JsonParserAndMapper mapper = new JsonParserFactory ().create ();
	
	@Setup
    public void setup()
    {
		File file = new File("src/test/resources/menu.json");
		try (InputStream is = new FileInputStream(file)) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(is, baos);
			baos.close();
			input = baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

	@Benchmark
    public MenuBar parseMenu() throws SQLException
    {
		return mapper.parse(MenuBar.class, new ByteArrayInputStream(input), Charset.defaultCharset());
    }
}
