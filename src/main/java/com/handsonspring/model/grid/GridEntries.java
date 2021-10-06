package com.handsonspring.model.grid;

import java.io.Serializable;
import java.util.List;

public class GridEntries implements Serializable {
      private List<Object[]> data;

      protected GridEntries() {}
      protected GridEntries(List<Object[]> data) {
            this.data = data;
      }

      public List<Object[]> getData() {
            return data;
      }
}