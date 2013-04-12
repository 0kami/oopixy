package at.ac.tuwien.infosys.www.pixy.conversion.nodes;

import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.infosys.www.phpparser.ParseNode;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

// *********************************************************************************
// CfgNodeAssignArray **************************************************************
// *********************************************************************************

public class CfgNodeAssignArray
extends CfgNode {

    private Variable left;

// CONSTRUCTORS ********************************************************************

    public CfgNodeAssignArray(Variable left, ParseNode node) {
        super(node);
        this.left = left;
    }

// GET *****************************************************************************

    public Variable getLeft() {
        return this.left;
    }

    public List<Variable> getVariables() {
        List<Variable> retMe = new LinkedList<Variable>();
        retMe.add(this.left);
        return retMe;
    }

//  SET ****************************************************************************

    public void replaceVariable(int index, Variable replacement) {
        switch (index) {
        case 0:
            this.left = replacement;
            break;
        default:
            throw new RuntimeException("SNH");
        }
    }
}