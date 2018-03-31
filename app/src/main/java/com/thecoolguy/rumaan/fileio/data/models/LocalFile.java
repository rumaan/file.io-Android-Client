package com.thecoolguy.rumaan.fileio.data.models;

import android.net.Uri;
import java.util.Objects;

/**
 * Use it to represent the current chosen local file
 */

public final class LocalFile {

  private String name;
  private long size;
  private Uri uri;

  public LocalFile(Uri uri, String name, long size) {
    this.name = name;
    this.size = size;
    this.uri = uri;
  }

  public LocalFile() {
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LocalFile localFile = (LocalFile) o;
    return size == localFile.size &&
        Objects.equals(name, localFile.name) &&
        Objects.equals(uri, localFile.uri);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name, size, uri);
  }

  @Override
  public String toString() {
    return "LocalFile{" +
        "name='" + name + '\'' +
        ", size=" + size +
        ", uri=" + uri +
        '}';
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public Uri getUri() {
    return uri;
  }

  public void setUri(Uri uri) {
    this.uri = uri;
  }
}
