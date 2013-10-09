package clustering;

import java.util.List;

import clustering.k_means.Cluster;

public class test {

  /**
   * @param args
   */
  public static void main(String[] args) {
    k_means km = new k_means();
    List<Cluster> clusterList = km.initialize();
    km.execute(clusterList);

  }

}
