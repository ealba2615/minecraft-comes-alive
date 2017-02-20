package mca.ai;

import mca.core.MCA;
import mca.core.minecraft.AchievementsMCA;
import mca.entity.EntityVillagerMCA;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import radixcore.constant.Time;
import radixcore.modules.RadixMath;

public class AIGrow extends AbstractAI
{
	private int timeUntilTickUpdate;
	private int freeTickUpdates;
	
	public AIGrow(EntityVillagerMCA entityHuman) 
	{
		super(entityHuman);
	}

	@Override
	public void onUpdateCommon() 
	{
		//This method runs on common to avoid use of packets or the data watcher to set the proper size. It can be a little dirty,
		//with the client lagging behind at most one minute, but the player should never notice this as any differences in hitbox
		//size after age increases by one is very small.
		
		if (owner.getIsChild())
		{
			if (timeUntilTickUpdate <= 0 || freeTickUpdates != 0)
			{
				if (owner.getAge() >= MCA.getConfig().childGrowUpTime && !owner.world.isRemote)
				{
					owner.getAIManager().disableAllToggleAIs();
					owner.setIsChild(false);
					
					for (Object obj : owner.world.playerEntities)
					{
						EntityPlayer player = (EntityPlayer)obj;
						
						if (owner.isPlayerAParent(player))
						{
							player.addStat(AchievementsMCA.childToAdult);
						}
					}
				}
	
				else
				{
					if (!owner.world.isRemote)
					{
						owner.setAge(owner.getAge() + 1);
					}
					
					float newHeight = 0.69F + (owner.getAge() * (1.8F - 0.69F) / MCA.getConfig().childGrowUpTime);
					owner.setSizeOverride(owner.width, newHeight);
				}
				
				timeUntilTickUpdate = Time.MINUTE;
				
				if (freeTickUpdates > 0)
				{
					freeTickUpdates--;
				}
			}
			
			else
			{
				timeUntilTickUpdate--;
			}
		}
	}

	@Override
	public void onUpdateClient() 
	{
	}

	@Override
	public void onUpdateServer() 
	{
	}

	@Override
	public void reset() 
	{	
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) 
	{	
		nbt.setInteger("timeUntilTickUpdate", timeUntilTickUpdate);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{	
		timeUntilTickUpdate = nbt.getInteger("timeUntilTickUpdate");
	}

	public void accelerate() 
	{
		freeTickUpdates = RadixMath.getNumberInRange(30, 60);
	}
}
