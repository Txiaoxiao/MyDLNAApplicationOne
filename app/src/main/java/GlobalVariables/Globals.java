package GlobalVariables;

import com.example.asus.mydlnaapplicationone.Device;

/**
 * Created by asus on 2018/3/29.
 */

public class Globals {

    private  static Globals instance  = new Globals();

    public static Globals getInstance()
    {
        return instance;
    }

    public static void setInstance(Globals instance)
    {
        Globals.instance = instance;
    }

    private Device selectedDevice;

    private Globals()
    {}

    public Device getSelectedDevice()
    {
        return  selectedDevice;
    }

    public  void setSelectedDevice(Device selectedDevice)
    {
        this.selectedDevice= selectedDevice;
    }




}
