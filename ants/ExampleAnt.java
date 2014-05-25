import ants.*;
import java.util.Random;

public class ExampleAnt implements Ant{
        private final Random r = new Random();
        private final int DIR_COUNT = 4;

        public ExampleAnt(){
            
        }

	public Action getAction(Surroundings surroundings){
            Direction randDir = Direction.values()[r.nextInt(DIR_COUNT)];
	    return Action.move(randDir);
	}
	
	public byte[] send(){
		return null;
	}
	
	public void receive(byte[] data){
		//Do nothing
	}

}
