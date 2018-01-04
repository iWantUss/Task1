/*
 * Copyright (C) 2018 iwantuss
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package matchmaker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 *
 * @author iwantuss
 */
public class MatchMaker {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
                
        count = 1;
        
        //Количество команд
        for (int i = 0; i < 500; i++) {
            for (int j = 0; j < maxPlayers; j++) {
                int rank = (int)(1+(Math.random()*30));
                    //rank = (int)(1+(Math.random()*rank));
                    
                    addPlayer("["+i+"-"+j+"]", rank);
            }
        }
        //mm.matches.forEach(val -> val.printMatch());
        
            matchmaking();
        
        
        
    }
    
    
    /**
     * Максимальное количество игроков в матче
     */
    private static final int maxPlayers = 64;
    /**
     * количество созданных матчей
     */
    private static int count = 0;
    
    
    /**
     * Список матчей
     */
    private static List<Match> matches = new ArrayList<>();
    
    public static void addPlayer(String UID, Integer rank){
        Player pl = new Player(UID, rank);
        for (Iterator<Match> it = matches.iterator(); it.hasNext();) {
            Match match = it.next();
            if(match.size()==0){
                it.remove();
                continue;
            }
            if(match.size()==maxPlayers){
                System.out.print(""+(count++)+" ");
                match.printMatch();
                continue;
            }
            if(match.addPlayer(pl)){
                return;
            }
        }
        matches.add(new Match(pl));
    }
    
    private static boolean addPlayer(Player pl){        
        
        for (Iterator<Match> it = matches.iterator(); it.hasNext();) {
            Match match = it.next();
            if(match.size()==0){
                it.remove();
                return false;
            }
            if(match.size()==maxPlayers){
                System.out.print(""+(count++)+" ");
                match.printMatch();
                return false;
            }
            return match.addPlayer(pl);
            
        }
        matches.add(new Match(pl));
        return true;
    }

    private static void matchmaking() {
        int countMatches = matches.size();
        while(countMatches!=1)
        for (int i = 1; i < countMatches; i++) {
            
            //Производим цикл по игрокам i-ой команды
            while (!matches.get(i).players.isEmpty()) {
                if(!addPlayer(matches.get(i).players.peek())){i--;break;}
                else {
                    
                    matches.get(i)
                            .players.poll();
                    
                }
            }
            if(matches.get(i).size()==0){
                matches.remove(i);
                countMatches--;i--;
                break;
            }
            if(matches.get(i).size()==maxPlayers){
                System.out.print(""+(count++)+" ");
                matches.get(i).printMatch();
                i--;
                continue;
            }
            
            
        }
        
        if(matches.get(0).size()==0){
                matches.remove(0);
            }
            if(matches.get(0).size()==maxPlayers){
                System.out.print(""+(count++)+" ");
                matches.get(0).printMatch();
                
            }
    }
}
