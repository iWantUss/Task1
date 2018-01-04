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

import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author iwantuss
 */
class Match {
    /**
     * Средний уровень игроков в матче
     */
    private Integer AvgRank;
    
    /**
     * Список игроков в матче
     */
    public Queue<Player> players;
    
    /**
     * Дата создания матча
     */
    private Long DateOfCreation;
    
    /**
     * Создатель матча
     */
    private Player creator;

    /**
     * Инициализация матча
     * @param creator 
     */
    public Match(Player creator) {
        this.creator = creator;
        players = new PriorityQueue<>((Player first, Player second) -> {
            if(first.getDateOfRegistration()>second.getDateOfRegistration()){
                return 1;
            }else if (first.getDateOfRegistration()<second.getDateOfRegistration()){
                return -1;
            } else return 0;
        }
        );
        players.add(creator);
        
        DateOfCreation = System.currentTimeMillis();
    }
    
    /**
     * Возвращает время которое прошло с момента даты создания матча
     * @return время которое прошло с момента даты создания матча
     */
    public Long getTimeCreationMatch(){
        return players.peek().getWaitingTime();
    }
    
    /**
     * Добавляет игрока в матч, если он соответствует условиям
     * @param player
     * @return true - если игрок был добавлен
     */
    public boolean addPlayer(Player player){
        return playerMeetsTheConditions(player)?players.add(player):false;
    }
    
    /**
     * Проверяет пользователя на соотвтетствие условиям этого матча
     * @param pl
     * @return 
     */
    private boolean playerMeetsTheConditions(Player pl){
        for (Player player : players) {
            int diffRank = Math.abs(player.getRank()-pl.getRank());
            double sumWaiting = player.getWaitingTime()+pl.getWaitingTime();
            if(diffRank>sumWaiting/5000)return false;
        }
        return true;
    }
    /**
     * Количество игроков в команде
     * @return Количество игроков в команде
     */
    int size() {
        return players.size();
    }
    
    public void printMatch(){
        System.out.print(this.getTimeCreationMatch()+"ms ");
        while(!players.isEmpty())
            players.poll().printPlayer();
        System.out.println("");
    }
    
    
    
    
    
}
