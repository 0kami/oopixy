package at.ac.tuwien.infosys.www.pixy.conversion.nodes;

import java.io.File;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

// *********************************************************************************
// CfgNodeIncludeEnd ***************************************************************
// *********************************************************************************

// indicates the end of an included section (inserted during include resolution)
public class CfgNodeIncludeEnd
extends CfgNode {

    private File file;  // file in which this node occurs
    private CfgNodeIncludeStart peer;

//  CONSTRUCTORS *******************************************************************

    // - instantiate CfgNodeIncludeStart
    // - instantiate CfgNodeIncludeEnd with the following constructor
    // this way, you don't have to call setPeer yourself
    public CfgNodeIncludeEnd(CfgNodeIncludeStart start) {
        super(start.getParseNode());
        start.setPeer(this);
        this.file = start.getContainingFile();
        this.peer = start;
    }


//  GET ****************************************************************************

    public File getFile() {
        return this.file;
    }

    public List<Variable> getVariables() {
        return Collections.emptyList();
    }

    public CfgNodeIncludeStart getPeer() {
        return this.peer;
    }

    public boolean isPeer(CfgNode node) {
        if (node == this.peer) {
            return true;
        } else {
            return false;
        }
    }

//  SET ****************************************************************************

    public void replaceVariable(int index, Variable replacement) {
    }

    public void setPeer(CfgNodeIncludeStart peer) {
        this.peer = peer;
    }
}