package org.azhur.java.interop;

import org.azhur.scala.interop.Compiler;
import scala.collection.immutable.*;

public class JavaCompiler implements Compiler {
    @Override
    public String language() {
        return "java";
    }

    @Override
    public void interop() {
        // use scala immutable List
        List nil = Nil$.MODULE$; // the empty list
        $colon$colon one = $colon$colon$.MODULE$.apply((Integer) 1, nil); // 1::nil
        $colon$colon two = $colon$colon$.MODULE$.apply((Integer) 2, one); // 2::1::nil
    }


    /**
     * WTF????
     */
    public void org$azhur$scala$interop$Compiler$_setter_$language_$eq(String lang) {
    }
}
