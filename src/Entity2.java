import java.util.ArrayList;

public class Entity2 extends Entity {
	
	private  static final ArrayList<Integer> adjecentNodes = new ArrayList<>();
    int minCost[] = new int[NetworkSimulator.NUMENTITIES];
    int entity = 2;
    int i , j;

    // Perform any necessary initialization in the constructor
    public Entity2() {
        System.out.println("Entity 2 \n");
        // Adds all adjacentNodes of node 2
        
        adjecentNodes.add(0);
        adjecentNodes.add(1);
        adjecentNodes.add(3);
        //Link costs between each node is initialized and sent to the distance table.
        
        for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
                distanceTable[i][j] = 999;
            }
        }
        for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
            minCost[i] = NetworkSimulator.cost[entity][i];
            distanceTable[entity][i] = minCost[i];
           
        }
        for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
            if (i != entity && minCost[i] != 999) {
                Packet packet = new Packet(entity, i, minCost);
                NetworkSimulator.toLayer2(packet);
            }
        }
        printDT();
        
        System.out.println("-----------------------------------------");
    }

    // Handle updates when a packet is received.  Students will need to call
    // NetworkSimulator.toLayer2() with new packets based upon what they
    // send to update.  Be careful to construct the source and destination of
    // the packet correctly.  Read the warning in NetworkSimulator.java for more
    // details.
    public void update(Packet p) {
        System.out.println("Entity 2 update");

        int source = p.getSource();
        System.out.println("Entity 2 : update : source:"+source); 
        for (j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
            distanceTable[source][j] = p.getMincost(j);
        }
        boolean updated = false;
        for (j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
            int updatedmincostEntity = distanceTable[entity][source];
            int updatedmincost = distanceTable[source][j];
            if (j != entity) {
                if (updatedmincostEntity > 0 && updatedmincost > 0) {
                    if (((updatedmincostEntity + updatedmincost) < distanceTable[entity][j])) {
                        distanceTable[entity][j] = updatedmincostEntity + updatedmincost;
                        updated = true;
                        linkCostChangeHandler(j, distanceTable[entity][j]);
                        System.out.println("Entity " + entity + " found a shortest path to node " + j + " via node " + source);
                    }
                }
            }
        }
        if (updated) {
            for (i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
                if (i != entity && minCost[i] != 999) {
                    Packet packet = new Packet(entity, i, minCost);
                    NetworkSimulator.toLayer2(packet);
                }
            }
        }
        printDT();
    }

    public void linkCostChangeHandler(int whichLink, int newCost) {
    	minCost[whichLink] = newCost;
    }

    public void printDT() {
        System.out.println();
        System.out.println("           via");
        System.out.println(" D2 |   0   1   3");
       // System.out.println(" D2 |   0   1   2   3");
        System.out.println("----+-------------------");
        for (int i = 0; i < NetworkSimulator.NUMENTITIES; i++) {
                       if (i == 2)
                        {
                            continue;
                        }

            System.out.print("   " + i + "|");
            for (int j = 0; j < NetworkSimulator.NUMENTITIES; j++) {
                                if (j == 2)
                                {
                                   continue;
                                }

                if (distanceTable[i][j] < 10) {
                    System.out.print("   ");
                } else if (distanceTable[i][j] < 100) {
                    System.out.print("  ");
                } else {
                    System.out.print(" ");
                }

                System.out.print(distanceTable[i][j]);
            }
            System.out.println();
        }
    }
}