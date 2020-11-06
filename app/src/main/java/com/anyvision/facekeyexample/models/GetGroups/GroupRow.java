package com.anyvision.facekeyexample.models.GetGroups;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

@Root(name = "GroupRow", strict = false)
public class GroupRow {

    @Element(name = "Description", required = false)
    private String Description;

    @Element(name = "Parameters" ,required = false)
    private String Parameters;

    @Element(name = "FullParameters", required = false)
    private String FullParameters;

    @Element(name = "Sound", required = false)
    private String Sound;

    @Element(name = "CustomExtension", required = false)
    private String CustomExtension;

    @Element(name="ColorInfo", required = false)
    private String ColorInfo;

    @Element(name = "Url",required = false)
    private String Url;

    @Element(name="Name", required = false)
    private String Name;

    @Element(name="CustomField1", required = false)
    private String CustomField1;

    @Element(name="Extension", required = false)
    private String Extension;

    @Element(name="CustomField2", required = false)
    private String CustomField2;

    @Element(name = "CustomField3", required = false)
    private String CustomField3;

    @Element(name="FullSound", required = false)
    private String FullSound;

    @Element(name="CustomField4", required = false)
    private String CustomField4;

    @Element(name = "CustomField5",required = false)
    private String CustomField5;

    @Element(name="FullSynoptic", required = false)
    private String FullSynoptic;

    @Element(name="Id", required = false)
    private String Id;

    @Element(name="Icon", required = false)
    private String Icon;

    @Element(name="Synoptic", required = false)
    private String Synoptic;

    @Element(name="FullDescription", required = false)
    private String FullDescription;

    public String getDescription() {
        return Description;
    }

    public String getParameters() { return Parameters; }

    public String getFullParameters() {
        return FullParameters;
    }


    public String getSound() {
        return Sound;
    }

    public String getCustomExtension() {
        return CustomExtension;
    }

    public String getColorInfo() {
        return ColorInfo;
    }

    public String getUrl() {
        return Url;
    }

    public String getName() {
        return Name;
    }

    public String getCustomField1() {
        return CustomField1;
    }

    public String getExtension() {
        return Extension;
    }

    public String getCustomField2() {
        return CustomField2;
    }

    public String getCustomField3() {
        return CustomField3;
    }

    public String getFullSound() {
        return FullSound;
    }

    public String getCustomField4() {
        return CustomField4;
    }

    public String getCustomField5() {
        return CustomField5;
    }

    public String getFullSynoptic() {
        return FullSynoptic;
    }

    public String getId() {
        return Id;
    }

    public String getIcon() {
        return Icon;
    }

    public String getSynoptic() {
        return Synoptic;
    }

    public String getFullDescription() {
        return FullDescription;
    }

//    @Override
//    public String toString()
//    {
//        return "ClassPojo [Description = "+Description+", Parameters = "+Parameters+", FullParameters = "+FullParameters+", Sound = "+Sound+", CustomExtension = "+CustomExtension+", ColorInfo = "+ColorInfo+", Url = "+Url+", Name = "+Name+", CustomField1 = "+CustomField1+", Extension = "+Extension+", CustomField2 = "+CustomField2+", CustomField3 = "+CustomField3+", FullSound = "+FullSound+", CustomField4 = "+CustomField4+", CustomField5 = "+CustomField5+", FullSynoptic = "+FullSynoptic+", Id = "+Id+", Icon = "+Icon+", Synoptic = "+Synoptic+", FullDescription = "+FullDescription+"]";
//    }
}
