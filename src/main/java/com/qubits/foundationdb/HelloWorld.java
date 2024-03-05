package com.qubits.foundationdb;

import com.foundationdb.Database;
import com.foundationdb.FDB;
import com.foundationdb.Transaction;
import com.foundationdb.tuple.Tuple;

public class HelloWorld {

  private static final FDB fdb;
  private static final Database db;

  static {
    fdb = FDB.selectAPIVersion(300);
    db = fdb.open();
  }

  public static void main(String[] args) {
    // Run an operation on the database
    db.run((Transaction tr) -> {
      tr.set(Tuple.from("hello").pack(), Tuple.from("world").pack());
      return null;
    });
    // Get the value of 'hello' from the database
    String hello = db.run((Transaction tr) -> {
      byte[] result = tr.get(Tuple.from("hello").pack()).get();
      return Tuple.fromBytes(result).getString(0);
    });
    System.out.println("Hello " + hello);
  }
}
