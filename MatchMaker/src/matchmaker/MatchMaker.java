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
        
        for (int i = 0; i < 100; i++) {//Количество команд
            for (int j = 0; j < MAX_PLAYERS; j++) {
                int rank = (int)(1+(Math.random()*30)); //генерация уровня нового игрока
                    
                    registrateNewPlayer("["+i+"-"+j+"]", rank);
            }
        }        
        matchmaking();
        
        
        
    }
    
    
    /**
     * Максимальное количество игроков в матче
     */
    private static final int MAX_PLAYERS = 8;    
    
    /**
     * Список матчей
     */
    private static List<Match> matches = new ArrayList<>();
    
    /**
     * Регистрирует нового пользователя и решает
     * добавить ли его в существующий матч или
     * создать для него новый, потому что он не подходит условиям матча.
     * @param UID
     * @param rank 
     */
    public static void  registrateNewPlayer(String UID, Integer rank){
        Player pl = new Player(UID, rank);
        for (Iterator<Match> it = matches.iterator(); it.hasNext();) {
            Match match = it.next();
            if(match.isEmpty()){
                it.remove();
                continue;
            }
            if(match.size()==MAX_PLAYERS){
                match.printAndRemovePlayers();
                continue;
            }
            if(match.addPlayer(pl)){
                return;
            }
        }
        matches.add(new Match(pl));
    }
    
    
    /**
     * Метод объединяющий созданные матчи до тех пор, пока
     * список матчей не станет пустым
     */
    private static void matchmaking() {
        int countMatches = matches.size();
        while(countMatches!=1)
        for (int i = 1; i < countMatches; i++) {
            
            //Производим цикл по игрокам i-ой команды
            while (!matches.get(i).isEmpty()) {
                if(replacePlayer(matches.get(i).players.peek())){
                    matches.get(i).players.poll();
                }
                else {i--;break;}
            }
            if(matches.get(i).isEmpty()){
                matches.remove(i);
                countMatches--;i--;
                break;
            }
            if(matches.get(i).size()==MAX_PLAYERS){
                matches.get(i).printAndRemovePlayers();
                i--;
            }
            
            
        }
        
        if(matches.get(0).isEmpty()){
            matches.remove(0);
        }
            
        if(matches.get(0).size()==MAX_PLAYERS){
            matches.get(0).printAndRemovePlayers();    
        }
    }
    /**
     * Перемещает игрока в самый старый матч, которому он соответствует
     * @param pl перемещаемый игрок
     * @return true если игрок был перемещен в новый матч
     */
    private static boolean replacePlayer(Player pl){        
        
        for (Iterator<Match> it = matches.iterator(); it.hasNext();) {
            Match match = it.next();
            if(match.size()==0){
                it.remove();
                return false;
            }
            if(match.size()==MAX_PLAYERS){
                match.printAndRemovePlayers();
                return false;
            }
            return match.addPlayer(pl);
            
        }
        matches.add(new Match(pl));
        return true;
    }
}
