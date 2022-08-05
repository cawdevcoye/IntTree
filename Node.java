
public class Node {
    private int level=-1;
    private Node zero,one, parent;
    private int count =0;
    private boolean containsValue = false;
    private int value=0;
    private int maxEncoderLength = -1;
    private int childrenAssignments =0;

    public Node(int encoderLen)
    {
        this.level = 0;
        this.maxEncoderLength=encoderLen;
        this.childrenAssignments =0;
    }
    public Node(Node parent)
    {
        this.parent=parent;
        this.level=parent.getLevel()+1;
        this.maxEncoderLength=parent.maxEncoderLength;
        this.childrenAssignments =0;
        if(level == this.maxEncoderLength+1)
            throw new RuntimeException("Encoder Dimensions Exceeded. ");
    }
    public Node getParent()
    {
        return this.parent;
    }
    public int getChildrenAssignments()
    {
        return this.childrenAssignments;
    }
    public void addChildAssignment()
    {
        this.childrenAssignments++;
    }
    public void removeOneChildAssignment()
    {
        this.childrenAssignments--;
        if(childrenAssignments < 0)
            throw new RuntimeException("Issue with removing values. ");
    }
    public int getLevel()
    {
        return this.level;
    }
    public boolean isRoot()
    {
        return (this.level==0 ? true : false);
    }
    public void addZero()
    {
        if(this.zero != null)
            throw new RuntimeException("Node already has a Zero Child. ");
        this.zero=new Node(this);
    }
    public void addOne()
    {
        if(this.one != null)
            throw new RuntimeException("Node already has a One Child. ");
        this.one=new Node(this);
    }
    public boolean hasOne()
    {
        return this.one == null ? false : true;
    }
    public boolean hasZero()
    {
        return this.zero == null ? false : true;
    }
    public Node getOne()
    {
        if(this.hasOne())
            return this.one;
        throw new RuntimeException("Cannot access ONE node, it doesnt exist. ");
    }
    public Node getZero()
    {
        if(this.hasZero())
            return this.zero;
        throw new RuntimeException("Cannot access ZERO node. It doesnt exist. ");
    }
    public int getCount()
    {
        if(!isActive())
            throw new RuntimeException("Node has not been set with an Integer yet. Cannot Access non-existing value. ");
        return this.count;
    }
    public int getValue()
    {
        if(isActive())
            return this.value;
        else
            throw new RuntimeException("Node has not been set with a value before. ");
    }
    public Node buildAndOrExploreZero()
    {
        if(!this.hasZero())
            this.addZero();
        if(this.zero.level > this.maxEncoderLength)
            throw new RuntimeException("Encoder length exceeded");
        return this.zero;
    }
    public Node buildAndOrExploreOne()
    {
        if(!this.hasOne())
            this.addOne();
        if(this.one.level > this.maxEncoderLength)
            throw new RuntimeException("Encoder length exceeded");
        return this.one;
    }
    public boolean isActive()
    {
        return (this.hasOne() || this.hasZero()) || this.level == this.maxEncoderLength;
    }
    public void addInteger(int integer)
    {
        if(!this.containsValue)
        {
            this.containsValue=true;
            this.value=integer;
        }
        else
            if (this.value != integer) {
                throw new RuntimeException("Number assigned to Node is different than value provided to method. ");
            }
        if(this.count==Integer.MAX_VALUE)
            throw new RuntimeException("Please don't add more than "+Integer.MAX_VALUE+" numbers to this node.");
        this.count++;
    }
    public void removeValue()
    {
        if(!this.containsValue)
            throw new RuntimeException("Removing Values when a value hasn't been set. ");
        if(this.count==0)
            throw new RuntimeException("Trying to remove a value that does not exist. ");

        this.count--;
    }
    public boolean contains(int value)
    {
        if(!this.isActive())
            throw new RuntimeException("Removing Values when a value hasn't been set. ");
        if(this.count==0)
            throw new RuntimeException("Trying to remove a value that does not exist. ");

        if(this.value!=value)
            return false;
        return true;
    }
    public boolean hasValue()
    {
        return (this.isActive() && this.count > 0);
    }
}