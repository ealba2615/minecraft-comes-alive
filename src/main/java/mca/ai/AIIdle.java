package mca.ai;

import mca.entity.EntityVillagerMCA;
import mca.enums.EnumMovementState;
import mca.enums.EnumProfessionSkinGroup;
import mca.enums.EnumSleepingState;
import net.minecraft.nbt.NBTTagCompound;
import radixcore.constant.Time;

public class AIIdle extends AbstractAI
{
	private int idleTicks;

	public AIIdle(EntityVillagerMCA owner) 
	{
		super(owner);
	}

	@Override
	public void onUpdateCommon() 
	{
	}

	@Override
	public void onUpdateClient() 
	{
	}

	@Override
	public void onUpdateServer() 
	{
		idleTicks++;
	
		if (idleTicks >= Time.MINUTE * 1 && owner.isInOverworld() && !owner.world.isDaytime() && owner.getProfessionSkinGroup() != EnumProfessionSkinGroup.Guard && owner.getMovementState() == EnumMovementState.STAY)
		{
			AISleep AISleep = owner.getAI(AISleep.class);
	
			if (!AISleep.getIsSleeping())
			{
				AISleep.setSleepingState(EnumSleepingState.SLEEPING);
			}
		}
	}

	@Override
	public void reset()
	{
		idleTicks = 0;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{
		nbt.setInteger("idleTicks", idleTicks);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		idleTicks = nbt.getInteger("idleTicks");
	}
}
