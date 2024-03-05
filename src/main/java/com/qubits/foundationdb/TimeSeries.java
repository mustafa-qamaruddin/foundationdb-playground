package com.qubits.foundationdb;

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.tuple.Tuple;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import static com.foundationdb.tuple.ByteArrayUtil.encodeInt;

public class TimeSeries {
  private static final FDB fdb;
  private static final Database db;

  static {
    fdb = FDB.selectAPIVersion(300);
    db = fdb.open();
  }

  private static final List<String> months = Arrays.asList(
    "january", "february", "march", "april", "may", "june",
    "july", "august", "september", "october", "november", "december"
  );

  private static final List<String> categories = Arrays.asList(
    "apparel", "hardware", "accessories", "grocery", "smartphones"
  );

  private static final List<String> regions = Arrays.asList(
    "north", "south", "west", "east", "center"
  );

  private static final String SALES_TABLE = "sales";

  public static void main(String[] args) {
    initDB(db);
  }

  private static void initDB(Database db) {
    Random rand = new Random();
    IntStream.range(2000, 2025).forEach(year -> {
      for (String month : months) {
        for (String category : categories) {
          for (String region : regions) {
            db.run(transaction -> {
              transaction.set(
                Tuple.from(SALES_TABLE, region, category, encodeInt(year), month).pack(),
                encodeFloat(rand.nextDouble())
              );
              return null;
            });
          }
        }
      }
    });
  }

  private static byte[] encodeFloat(double v) {
    return new byte[8];
  }
}
