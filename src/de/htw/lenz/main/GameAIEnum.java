//package de.htw.lenz.main;
//
//import de.htw.lenz.AI.ErrorAI;
//import de.htw.lenz.AI.GameAI;
//import de.htw.lenz.AI.MinMaxAI;
//import de.htw.lenz.AI.RandomAI;
//
//public enum GameAIEnum implements GameAIBuilder{
//  MinMaxAI {
//    @Override
//    public GameAI createGameAI() { return new MinMaxAI(); }
//  },
//  RandomAI {
//    @Override
//    public GameAI createGameAI() { return new RandomAI(); }
//  },
//  ErrorAI {
//    @Override
//    public GameAI createGameAI() { return new ErrorAI(); }
//  }
//}
