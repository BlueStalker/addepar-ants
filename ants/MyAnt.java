import ants.*;
import java.util.Random;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.awt.Point;
import java.lang.RuntimeException;

/*
!javac -classpath ../AntsSimulator.jar %
!java -jar ../AntsSimulator.jar 1>../log/console.log 2>../log/error.log
/home/amartin/addepar-ants/ants/MyAnt.class
*/

public class MyAnt implements Ant{
        private final Random r = new Random();
        private final Map<Direction,Tile> tiles = initTiles();
        private final List<Direction> travelables = new ArrayList<Direction>(4);
        private final Map<Point,Tile> map = new HashMap<Point,Tile>();
        private Point pos;

        private final int MODE_EXPLORE = 0;
        private final int MODE_TARGET_DIRECT = 1;
        private final int MODE_TARGET_EXPLORE = 2;
        
        private int mode;
        private Point targetPoint = null;
        private LinkedList<Point> path = new LinkedList<Point>();

        public MyAnt(){
            System.out.println("construct");
            this.mode = MODE_EXPLORE;

            this.pos = new Point(0,0);
        }

	public Action getAction(Surroundings surroundings){
            updateState(surroundings);

            Action action;
            switch(this.mode){
            case MODE_EXPLORE:
                action = explore();
                break;
            case MODE_TARGET_DIRECT:
                //go to home or food directly, if you find an obstruction redo path
                action = targetDirect();
                break;
            case MODE_TARGET_EXPLORE:
                //go to target directly, if you find an objstruction, explore where you are
                action = targetExplore();
                break;
            default:
                throw new RuntimeException("Invalid ant mode.");
            }
            //Direction randDir = travelables.get(r.nextInt(travelables.size()));
            this.pos = dirPoint(action.getDirection());
	    return action;
	}
	
        private Action explore(){
            //move to any adjacent square with unexplored adjacent squares
            //pick randomly
            //if none be globally random
            //become TARGET_EXPLORE
            LinkedList<Direction> exploreDirs = new LinkedList<Direction>();
            for(Direction dir : this.travelables){
                Point destination = this.dirPoint(dir);
                if(hasAdjacentUnknowns(destination)){
                    exploreDirs.add(dir);
                }
            }
            Direction exploreDirection;
            if(exploreDirs.size() > 0){
                exploreDirection = exploreDirs.get(r.nextInt(exploreDirs.size()));
            }else{
                List<Point> knownPoints = new ArrayList<Point>(this.map.keySet());
                Collections.shuffle(knownPoints);
                Point targetPoint = null;
                for(Point p : knownPoints){
                    if(hasAdjacentUnknowns(p)){
                        targetPoint = p;
                        break;
                    }
                }
                if(targetPoint == null){
                    println("this was unexpected");
                    targetPoint = new Point(0,0);
                }

                setTarget(targetPoint);
                
                return Action.move(path.remove());
            }
            return Action.move(exploreDirection);
        }

        void setTarget(Point target){
            
        }

        private void aStar(Point target){
            Point start = this.pos;
            Set<Point> closed = new HashSet<Point>();
            Set<Point> open = new HashSet<Point>();
            int score = 0;
            open.add(start);

            while(open.size() > 0){
                
            }

        }

        private boolean hasAdjacentUnknowns(Point p){
            for(Direction edgeDir : Direction.values()){
                Point edgePoint = this.dirPoint(p,edgeDir);
                if(!this.map.containsKey(edgePoint)){
                    return true;
                }
            }
            return false;
        }

        private Action targetDirect(){
            Direction randDir = travelables.get(r.nextInt(travelables.size()));
            return Action.move(randDir);
        }

        private Action targetExplore(){
            Direction randDir = travelables.get(r.nextInt(travelables.size()));
            return Action.move(randDir);
        }

	public byte[] send(){
            byte[] data = new byte[4 * this.map.size()];
            int pointer = 0;
            for(Map.Entry<Point, Tile> entry : this.map.entrySet()){
                data[pointer] = (byte)entry.getKey().getX();
                pointer++;
                data[pointer] = (byte)entry.getKey().getY();
                pointer++;
                data[pointer] = (byte)entry.getValue().getAmountOfFood();
                pointer++;
                if(entry.getValue().isTravelable()){
                    data[pointer] = (byte)1;
                }else{
                    data[pointer] = (byte)0;
                }
                pointer++;
            }
            return data;
	}
	
        private void println(String s){
            System.out.println(s);
        }

	public void receive(byte[] data){
            int mapSize = this.map.size();
            for(int i = 0; i < data.length; i+=4){
                Point newPoint = new Point((int)data[i],(int)data[i+1]);
                if(!this.map.containsKey(newPoint)){
                    int amountOfFood = (int)data[i+2];
                    boolean isTravelable;
                    if((int)data[i+3] == 1){
                        isTravelable = true;
                    }else{
                        isTravelable = false;
                    }
                    this.map.put(newPoint,new MyTile(amountOfFood,0,isTravelable));
                }
            }
            println(String.format("Map size was %s now %s",mapSize,this.map.size()));
	}

        private void updateState(Surroundings surroundings){
            travelables.clear();
            for(Direction dir : tiles.keySet()){
                Tile tile = surroundings.getTile(dir);
                this.map.put(this.dirPoint(dir),tile);
                this.tiles.put(dir, tile);
                if(tile.isTravelable()){
                    this.travelables.add(dir);
                }
            }
        }

        private static Map<Direction,Tile> initTiles(){
            Map<Direction,Tile> result = new HashMap<Direction,Tile>();
            for(Direction dir : Direction.values()){
                result.put(dir,null);
            }
            return result;
        }

        private Point dirPoint(Point p, Direction dir){
            Point newPos = p.getLocation();
            if(dir != null){
                switch(dir){
                    case NORTH: 
                        newPos.translate(0,-1);
                        break;
                    case EAST: 
                        newPos.translate(1,0);
                        break;
                    case SOUTH: 
                        newPos.translate(0,1);
                        break;
                    case WEST: 
                        newPos.translate(-1,0);
                        break;
                }
            }
            return newPos;
        }
        
        private Point dirPoint(Direction dir){
            return this.dirPoint(this.pos,dir);
        }
}

        /*
        int a = 129;
        byte b = (byte)a;
        int a_prime = (int)b;
        System.out.println(a_prime);
        boolean bool = false;
        byte boolByte;
        if(bool){
            boolByte = (byte)1;
        } else {
            boolByte = (byte)0;
        }
        boolean bool_prime;
        if(boolByte == (byte)1){
            bool_prime = true;
        } else {
            bool_prime = false;
        }
        System.out.println(bool_prime);
        */
