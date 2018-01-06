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
 * Матч из игроков
 * @author iwantuss
 */
class Match {    
    /**
     * Список игроков в матче отсортированые в порядке регистрации
     */
    public Queue<Player> players = new PriorityQueue<>(
            (Player first, Player second) -> {
                if(first.getDateOfRegistration()>second.getDateOfRegistration()){
                    return 1;
                }else 
                    return 0;
        });

    /**
     * Инициализация матча 
     * @param creator 
     */
    public Match(Player creator) { 
        players.add(creator);
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
        return playerMeetsTheConditions(player) ? players.add(player) : false;
    }
    
    /**
     * Проверяет пользователя на соотвтетствие условиям этого матча
     * @param pl
     * @return 
     */
    private boolean playerMeetsTheConditions(Player pl){
        //Последовательный stream работает быстрее parallelStream
        return players.stream().allMatch((player) -> {
            int diffRank = Math.abs(player.getRank()-pl.getRank());
            if(diffRank==0)return true;
            double sumWaiting = player.getWaitingTime()+pl.getWaitingTime();
            return diffRank <= sumWaiting/5000;
        });
        
    }
    /**
     * Количество игроков в команде
     * @return Количество игроков в команде
     */
    int size() {
        return players.size();
    }
    /**
     * @return false если матч пуст (в нем нет игроков)
     */
    boolean isEmpty(){
        return players.isEmpty();
    }
    
    /**
     * Выводит в консоль {@see #getTimeCreationMatch время создания матча}
     * и выводит {@see Player#printPlayer данные игроков} в порядке добавления в матч
     * после чего удаляет их из очереди.
     */
    public void printAndRemove(){
        
        System.out.print("["+(MatchMaker.COUNT++)+"]"+this.getTimeCreationMatch()+"ms ");
        while(!players.isEmpty())
            players.poll().printPlayer();
        System.out.println("");
    }
     
}
