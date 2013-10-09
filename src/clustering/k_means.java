package clustering;

import java.util.ArrayList;
import java.util.List;

public class k_means {
  
  private final int clusterNum = 3;
  private final int[] testData = {1, 20000, 10, 5000, 10101, 1500, 2000, 100, 10000, 1000, 15000, 2};
  private Member[] originalData = null;
  
  public k_means() {
    originalData = new Member[testData.length];
    for (int i = 0; i < testData.length; i++) {
      originalData[i] = new Member(testData[i], 0);
    }
  }
  
  public k_means(int[] data) {
    originalData = new Member[testData.length];
    for (int i = 0; i < testData.length; i++) {
      originalData[i] = new Member(data[i], 0);
    }
  }
  
  public List<Cluster> initialize() {
    List<Cluster> clusterList = new ArrayList<Cluster>();
    for (int i = 0; i < clusterNum; i++) {
      Cluster cluster = new Cluster();
      cluster.setClusterNumber(i);
      clusterList.add(cluster);
    }
    assignRandom(clusterList);
    for (int i = 0; i < clusterList.size(); i++) {
      System.out.println("Cluster : " + i);
      for (int j = 0; j < clusterList.get(i).members.size(); j++) {
        System.out.print(clusterList.get(i).getMember(j).getValue() + ",");
      }
      System.out.println("");
    }
    return clusterList;
  }
  
  private void assignRandom(List<Cluster> list) {
    for (int i = 0; i < originalData.length; i++) {
      int num = (int)(Math.random() * clusterNum);
      originalData[i].setClusterNumber(num);
      list.get(num).addMember(originalData[i]);
    }
  }
  
  public void execute(List<Cluster> list) {
    while(executePartitioning(list));
    for (int i = 0; i < list.size(); i++) {
      System.out.println("Cluster(after) : " + i);
      for (int j = 0; j < list.get(i).members.size(); j++) {
        System.out.print(list.get(i).getMember(j).getValue() + ",");
      }
      System.out.println("");
    }
  }
  
  private boolean executePartitioning(List<Cluster> list) {
    boolean res = false;
    double[] median = new double[clusterNum];
    System.out.println("executePartitioning : 1");
    for (int i = 0; i < clusterNum; i++) {
      median[i] = list.get(i).getMedian();
      System.out.println("median(" + i + ") : " + median[i]);
      list.get(i).printCluster();
    }
    System.out.println("executePartitioning : 2");
    for (int i = 0; i < originalData.length; i++) {
      int j = closestCluster(median, originalData[i]);
      int prevCluster = originalData[i].getClusterNumber();
      if ( j != -1 && list.get(prevCluster).getNumberOfMembers() > 1) {
        res = true;
        if(list.get(prevCluster).removeMember(originalData[i])) {
          originalData[i].setClusterNumber(j);
          list.get(j).addMember(originalData[i]);
        }
      }
    }
    System.out.println("executePartitioning : 3");
    return res;
  }
  
  private int closestCluster(double[] med, Member mem) {
    int res = -1;
    double min = -1;
    for (int i = 0; i < med.length; i++) {
      double temp = calculateDistance(mem.getValue(), med[i]);
      if (min == -1 || temp < min) {
        min = temp;
        res = i;
      }
    }
    if (res == mem.getClusterNumber()) {
      return -1;
    }
    return res;
  }
  
  private double calculateDistance(int source, double median) {
    return Math.abs((double)source - median);
  }
  
  public class Cluster {
    public int number;
    public List<Member> members;
    
    public Cluster() {
      members = new ArrayList<Member>();
    }
    
    public void addMember(Member mem) {
      members.add(mem);
    }
    
    public boolean removeMember(Member mem) {
      return members.remove(mem);
    }
    
    public Member getMember(int index) {
      if (index > members.size()) {
        return null;
      }
      return members.get(index);
    }
    
    public double getMedian() {
      double ret = 0;
      for (int i = 0; i < members.size(); i++) {
        ret += members.get(i).getValue();
      }
      return ret / members.size();
    }
    public void setClusterNumber(int n) {
      number = n;
    }
    public int getClusterNumber() {
      return number;
    }
    public int getNumberOfMembers() {
      return members.size();
    }
    public void printCluster() {
      System.out.println("cluster number : " + number);
      System.out.println("  member : ");
      for (int i = 0; i < members.size(); i++) {
        System.out.print(members.get(i).getValue() + ", ");
      }
      System.out.println();
    }
  }
  
  private class Member {
    private int value;
    private int clusterNumber;
    
    public Member() {
      value = 0;
      clusterNumber = 0;
    }
    public Member(int v, int c) {
      value = v;
      clusterNumber = c;
    }
    public void setValue(int v) {
      value = v;
    }
    public void setClusterNumber(int c) {
      clusterNumber = c;
    }
    public int getValue() {
      return value;
    }
    public int getClusterNumber() {
      return clusterNumber;
    }
  }
  
}
