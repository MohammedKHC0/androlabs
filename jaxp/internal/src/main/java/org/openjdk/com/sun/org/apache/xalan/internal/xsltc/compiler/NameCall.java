/*
 * reserved comment block
 * DO NOT REMOVE OR ALTER!
 */
/*
 * Copyright 2001-2004 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * $Id: NameCall.java,v 1.2.4.1 2005/09/02 10:19:11 pvedula Exp $
 */

package org.openjdk.com.sun.org.apache.xalan.internal.xsltc.compiler;

import org.openjdk.com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import org.openjdk.com.sun.org.apache.bcel.internal.generic.InstructionList;
import org.openjdk.com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import org.openjdk.com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;

import java.util.Vector;

/**
 * @author Jacek Ambroziak
 * @author Santiago Pericas-Geertsen
 * @author Morten Jorgensen
 */
final class NameCall extends NameBase {

    /** Handles calls with no parameter (current node is implicit parameter). */
    public NameCall(QName fname) {
        super(fname);
    }

    /** Handles calls with one parameter (either node or node-set). */
    public NameCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    /** Translate code that leaves a node's QName (as a String) on the stack */
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        final ConstantPoolGen cpg = classGen.getConstantPool();
        final InstructionList il = methodGen.getInstructionList();

        final int getName = cpg.addInterfaceMethodref(DOM_INTF, GET_NODE_NAME, GET_NODE_NAME_SIG);
        super.translate(classGen, methodGen);
        il.append(new INVOKEINTERFACE(getName, 2));
    }
}
