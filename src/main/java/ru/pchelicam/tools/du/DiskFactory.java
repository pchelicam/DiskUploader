package ru.pchelicam.tools.du;

public class DiskFactory {
    /**
     * Factory method
     * @return a new object of class,
     * depends on inputted type of the disk
     */
    public static IActionDisk getDisk(TypeDiskEnum type){
        IActionDisk disk = null;
        if(type.equals(TypeDiskEnum.YA)){
            disk = new DiskActionYandex();
        }
        if(type.equals(TypeDiskEnum.DB)){
            disk = new DiskActionDropbox();
        }
        return disk;
    }
}

