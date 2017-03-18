 package com.github.jotask.neat.jneat.util;

 import com.github.jotask.neat.jneat.genetics.Genome;
import com.github.jotask.neat.jneat.genetics.Synapse;
import com.github.jotask.neat.util.JRandom;

import java.util.HashMap;

 /**
 * Checker
 *
 * @author Jose Vives Iznardo
 * @since 18/03/2017
 */
public class Checker {

    public static void crossover(final Genome mother, final Genome father, final Genome child){
        if(child.getGenes().isEmpty()){
            return;
        }
        System.out.println("Mother: " + mother.toString());
        System.out.println("Father: " + father.toString());
        System.out.println("Child: " + child.toString());
    }

    public static Genome crossover(final Genome mother, final Genome father){
        final Genome child = new Genome();

        outerLoop: for(final Synapse gene1: mother.getGenes()){
            for(final Synapse gene2: father.getGenes()){
                if(gene1.getInnovation() == gene2.getInnovation()){
                    if(JRandom.nextBoolean() && gene2.isEnabled()){
                        child.addLink(gene2);
                        continue outerLoop;
                    }else {
                        break;
                    }
                }
            }
            child.addLink(gene1);
        }

        final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();


        for(final Synapse s: child.getGenes()){
            add(map, s.getInput());
            add(map, s.getOutput());
        }

        System.out.println(child.getGenes().toString());

        child.maxNeuron = Math.max(mother.maxNeuron, father.maxNeuron);
        child.step_size = mother.step_size;
        System.out.println("--------");
        return child;
    }

     private static void genes(final Genome gene){
        if(gene.getGenes().isEmpty())
            return;
         final HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for(final Synapse s: gene.getGenes()){
            final int input = s.getInput();
            final int output = s.getOutput();
            add(map, input);
            add(map, output);
        }
        System.out.println(map.toString());
     }

     private static void add(final HashMap<Integer, Integer> map, final int index){

         if(map.containsKey(index)){
             int tmp = map.get(index);
             tmp++;
             map.put(index, tmp);
         }else{
             map.put(index, 1);
         }

     }

}
