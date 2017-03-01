package com.gam.nocr.ems.util;

import java.io.Closeable;
import java.io.Flushable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {

	public static String getStringFromException(Exception exception) {

		if (exception == null)

			return null;

		StringWriter stringWriter = null;

		PrintWriter printWriter = null;

		try {

			stringWriter = new StringWriter();

			printWriter = new PrintWriter(stringWriter);

			exception.printStackTrace(printWriter);

			return stringWriter.toString();

		} finally {

			flush(printWriter, stringWriter);

			close(printWriter, stringWriter);

		}

	}

	private static void flush(Flushable... flushables) {

		if (flushables == null)

			return;

		for (Flushable flushable : flushables) {

			if (flushable != null)

				try {

					flushable.flush();

				} catch (IOException exception) {

					exception.printStackTrace();

				}

		}

	}

	private static void close(Closeable... closeables) {

		if (closeables == null)

			return;

		for (Closeable closeable : closeables) {

			if (closeable != null)

				try {

					closeable.close();

				} catch (IOException exception) {

					exception.printStackTrace();

				}

		}

	}

}
