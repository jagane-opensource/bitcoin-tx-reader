/*
 * Copyright (c) 2020, InfinStor, Inc.
 * Licensed under the terms of the Apache License Version 2.0, January 2004
 * See the file LICENSE-2.0.txt at the top of this repository
 */
package com.infinstor.bitcoin.txreader;

import java.util.Formatter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.ZMsg;
import org.zeromq.ZFrame;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.params.MainNetParams;

import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

public class BitcoinTxReader {
  public static void main(String args[]) {

    if (args.length != 2) {
      System.err.println("Usage: BitcoinTxReader s3-bucketname objectname");
      System.exit(-1);
    }
    String bucketname = args[0];
    String objectname = args[1];

    AmazonS3 s3client = AmazonS3ClientBuilder.defaultClient();

    byte[] contents = null;
    try {
      contents = readFromBucket(s3client, bucketname, objectname);
    } catch (IOException ioe) {
      System.err.println("Error. Caught " + ioe);
      System.exit(-1);
    }
    if (contents != null) {
      NetworkParameters params = MainNetParams.get();
      Transaction transaction = null;
      try {
        transaction = new Transaction(params, contents);
      } catch (Exception ex) {
        System.err.println("Caught " + ex
          + " trying to create a Transaction object from byte array of "
          + contents.length + " read from S3");
      }
      System.out.println("Transaction=" + transaction);
      System.exit(0);
    }
    System.err.println("Error. Could not read object contents");
    System.exit(-1);
  }

  private static byte[] readFromBucket(AmazonS3 s3client, String bucketname, String objectname)
      throws IOException {
    S3Object obj = s3client.getObject(bucketname, objectname);
    S3ObjectInputStream ins = obj.getObjectContent();

    byte buf[] = new byte[4096];
    int bytes = ins.read(buf, 0, 4096);
    if (bytes >= 0)
      return Arrays.copyOf(buf, bytes);
    else
      return null;
  }
}
