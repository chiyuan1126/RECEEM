package cn.edu.njust.chiyuan.conjunction.pojo;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Graph<String, DefaultEdge> stringGraph = createStringGraph();

        // note undirected edges are printed as: {<v1>,<v2>}
        System.out.println("-- toString output");
        System.out.println(stringGraph.toString());
        System.out.println();
        
      AllDirectedPaths adr=new AllDirectedPaths(stringGraph);
      List<GraphPath<String,DefaultEdge>> as =adr.getAllPaths("v3", "v1", true, null);
      
       for(int i=0;i<as.size();i++){
    	   List<String> allvs=as.get(i).getVertexList();
    	   for(String al:allvs){
    		   System.out.println("=>"+al);
    	   }
       }
	}
	
	private static Graph<String, DefaultEdge> createStringGraph()
    {
        Graph<String, DefaultEdge> g = new DefaultDirectedGraph<>(DefaultEdge.class);

        String v1 = "v1";
        String v2 = "v2";
        String v3 = "v3";
        String v4 = "v4";

        // add the vertices
        g.addVertex(v1);
        g.addVertex(v2);
        g.addVertex(v3);
        g.addVertex(v4);

        // add edges to create a circuit
        g.addEdge(v1, v2);
        g.addEdge(v2, v3);
        g.addEdge(v3, v4);
        g.addEdge(v4, v1);

        return g;
    }

}
