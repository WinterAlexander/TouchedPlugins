package me.winterguardian.core.entity.custom.rideable.v1_8_R3;

import me.winterguardian.core.entity.custom.rideable.RideableEntity;
import net.minecraft.server.v1_8_R3.Block;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.EnchantmentManager;
import net.minecraft.server.v1_8_R3.EntityBoat;
import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.TrigMath;

import java.lang.reflect.Field;

public class RideableBoat extends EntityBoat implements RideableEntity
{
	public boolean ar;
	public int as;
	public int at;
	public int hurtTicks;
	public int av;
	public float aw;
	public int deathTicks;
	public float ay;
	public float az;
	public float aA;
	public float aB;
	public float aC;
	public int maxNoDamageTicks = 20;
	public float aE;
	public float aF;
	public float aG;
	public float aH;
	public float aI;
	public float aJ;
	public float aK;
	public float aL;
	public float aM = 0.02F;
	public EntityHuman killer;
	protected int lastDamageByPlayerTime;
	protected boolean aP;
	protected int ticksFarFromPlayer;
	protected float aR;
	protected float aS;
	protected float aT;
	protected float aU;
	protected float aV;
	protected int aW;
	public float lastDamage;
	protected boolean aY;
	public float aZ;
	public float ba;
	protected float bb;
	protected int bc;
	protected double bd;
	protected double be;
	protected double bf;
	protected double bg;
	protected double bh;
	public boolean updateEffects = true;
	public EntityLiving lastDamager;
	public int hurtTimestamp;
	private float bm;
	private int bn;
	public int expToDrop;
	public int maxAirTicks = 300;
	
	private int datawatcher9;
	
	private float climbHeight, jumpHeight, jumpThrust, speed, backwardSpeed, sidewaySpeed;

	public RideableBoat(org.bukkit.World world, double x, double y, double z)
	{
		this(((CraftWorld)world).getHandle(), x, y, z);
	}

	public RideableBoat(World world, double x, double y, double z)
	{
		super(world, x, y, z);
		
		this.aH = ((float)((Math.random() + 1.0D) * 0.009999999776482582D));
		setPosition(this.locX, this.locY, this.locZ);
		this.aG = ((float)Math.random() * 12398.0F);
		this.yaw = ((float)(Math.random() * 3.1415927410125732D * 2.0D));
		this.aK = this.yaw;
		this.S = 0.6F;
	
	    this.datawatcher9 = 0;
	    
	    
		this.climbHeight = 1f;
		this.jumpHeight = 1f;
		this.jumpThrust = 1f;
		this.speed = 1f;
		this.backwardSpeed = 0.25f;
		this.sidewaySpeed = 0.4f;


	}

	@Override
	public void aA()
	{
		this.H = true;
		this.fallDistance = 0;
	}

	public void g(float sideMot, float forMot)
	{
		if(this.passenger == null || !(this.passenger instanceof EntityHuman))
		{
			this.S = 0.6f; 
			superg(sideMot, forMot);
			return;
		}
		
		this.lastYaw = this.yaw = this.passenger.yaw;
		this.pitch = this.passenger.pitch * 0.75f;
		if(this.pitch > 0)
			this.pitch = 0;
		this.setYawPitch(this.yaw, this.pitch);
		this.aK = this.aI = this.yaw;
	
		this.S = this.climbHeight; 
	
		boolean jump = false;
		
		try
		{
			Field field = EntityLiving.class.getDeclaredField("aY");
			field.setAccessible(true);
			jump = (boolean) field.get(this.passenger);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		sideMot = ((EntityLiving) this.passenger).aZ;
		forMot = ((EntityLiving) this.passenger).ba;

		if (forMot < 0.0F)
			forMot *= this.backwardSpeed;
	
		sideMot *= this.sidewaySpeed;
	 
		if(jump)
			if(this.inWater)
				this.bG();
			else if(this.onGround && this.jumpHeight != 0 && this.jumpThrust != 0)
			{
				this.motY = this.jumpHeight / 2;
				this.motZ = Math.cos(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal X
				this.motX = Math.sin(Math.toRadians(-this.yaw)) * this.jumpThrust * forMot; //normal Y
			}

		this.bm = this.speed / 5;
		superg(sideMot, forMot);
	}
	
	public void t_()
	{
	    K();
	    if (!this.world.isClientSide)
	    {
	      int i = this.datawatcher9;
	      if (i > 0)
	      {
	        if (this.at <= 0) {
	          this.at = (20 * (30 - i));
	        }
	        this.at -= 1;
	        if (this.at <= 0)
	        	this.datawatcher9 = i - 1;
	        
	      }
	    }
	    entityLivingM();
	    double d0 = this.locX - this.lastX;
	    double d1 = this.locZ - this.lastZ;
	    float f = (float)(d0 * d0 + d1 * d1);
	    float f1 = this.aI;
	    float f2 = 0.0F;
	    
	    this.aR = this.aS;
	    float f3 = 0.0F;
	    if (f > 0.0025000002F)
	    {
	      f3 = 1.0F;
	      f2 = (float)Math.sqrt(f) * 3.0F;
	      
	      f1 = (float)TrigMath.atan2(d1, d0) * 180.0F / 3.1415927F - 90.0F;
	    }
	    if (this.az > 0.0F) {
	      f1 = this.yaw;
	    }
	    if (!this.onGround) {
	      f3 = 0.0F;
	    }
	    this.aS += (f3 - this.aS) * 0.3F;
	    this.world.methodProfiler.a("headTurn");
	    f2 = h(f1, f2);
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("rangeChecks");
	    while (this.yaw - this.lastYaw < -180.0F) {
	      this.lastYaw -= 360.0F;
	    }
	    while (this.yaw - this.lastYaw >= 180.0F) {
	      this.lastYaw += 360.0F;
	    }
	    while (this.aI - this.aJ < -180.0F) {
	      this.aJ -= 360.0F;
	    }
	    while (this.aI - this.aJ >= 180.0F) {
	      this.aJ += 360.0F;
	    }
	    while (this.pitch - this.lastPitch < -180.0F) {
	      this.lastPitch -= 360.0F;
	    }
	    while (this.pitch - this.lastPitch >= 180.0F) {
	      this.lastPitch += 360.0F;
	    }
	    while (this.aK - this.aL < -180.0F) {
	      this.aL -= 360.0F;
	    }
	    while (this.aK - this.aL >= 180.0F) {
	      this.aL += 360.0F;
	    }
	    this.world.methodProfiler.b();
	    this.aT += f2;
	}
	
	protected float h(float f, float f1)
	{
		float f2 = MathHelper.g(f - this.aI);

		this.aI += f2 * 0.3F;
		float f3 = MathHelper.g(this.yaw - this.aI);
		boolean flag = (f3 < -90.0F) || (f3 >= 90.0F);
	    if (f3 < -75.0F) {
	      f3 = -75.0F;
	    }
	    if (f3 >= 75.0F) {
	      f3 = 75.0F;
	    }
	    this.aI = (this.yaw - f3);
	    if (f3 * f3 > 2500.0F) {
	      this.aI += f3 * 0.2F;
	    }
	    if (flag) {
	      f1 *= -1.0F;
	    }
	    return f1;
	}
	
	public void entityLivingM()
	{
		if (this.bn > 0)
			this.bn -= 1;
	    
	    if (this.bc > 0)
	    {
	      double d0 = this.locX + (this.bd - this.locX) / this.bc;
	      double d1 = this.locY + (this.be - this.locY) / this.bc;
	      double d2 = this.locZ + (this.bf - this.locZ) / this.bc;
	      double d3 = MathHelper.g(this.bg - this.yaw);
	      
	      this.yaw = ((float)(this.yaw + d3 / this.bc));
	      this.pitch = ((float)(this.pitch + (this.bh - this.pitch) / this.bc));
	      this.bc -= 1;
	      setPosition(d0, d1, d2);
	      setYawPitch(this.yaw, this.pitch);
	    }
	    else
	    {
	      this.motX *= 0.98D;
	      this.motY *= 0.98D;
	      this.motZ *= 0.98D;
	    }
	    if (Math.abs(this.motX) < 0.005D) {
	      this.motX = 0.0D;
	    }
	    if (Math.abs(this.motY) < 0.005D) {
	      this.motY = 0.0D;
	    }
	    if (Math.abs(this.motZ) < 0.005D) {
	      this.motZ = 0.0D;
	    }
	    this.world.methodProfiler.a("ai");
	    this.world.methodProfiler.a("newAi");
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("jump");
	    if (this.aY)
	    {
	      if (V())
	      {
	        bG();
	      }
	      else if (ab())
	      {
	        bH();
	      }
	      else if ((this.onGround) && (this.bn == 0))
	      {
	        bF();
	        this.bn = 10;
	      }
	    }
	    else {
	      this.bn = 0;
	    }
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("travel");
	    this.aZ *= 0.98F;
	    this.ba *= 0.98F;
	    this.bb *= 0.9F;
	    g(this.aZ, this.ba);
	    this.world.methodProfiler.b();
	    this.world.methodProfiler.a("push");
	    this.world.methodProfiler.b();
	}
	
	public void superg(float f, float f1)
	{
		if(V())
		{
			double d0 = this.locY;
	        float f3 = 0.8F;
	        float f4 = 0.02F;
	        float f2 = EnchantmentManager.b(this);
	        if (f2 > 3.0F) {
	          f2 = 3.0F;
	        }
	        if (!this.onGround) {
	          f2 *= 0.5F;
	        }
	        if (f2 > 0.0F)
	        {
	          f3 += (0.54600006F - f3) * f2 / 3.0F;
	          f4 += (this.bm * 1.0F - f4) * f2 / 3.0F;
	        }
	        a(f, f1, f4);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= f3;
	        this.motY *= 0.800000011920929D;
	        this.motZ *= f3;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else if (ab())
	      {
	        double d0 = this.locY;
	        a(f, f1, 0.02F);
	        move(this.motX, this.motY, this.motZ);
	        this.motX *= 0.5D;
	        this.motY *= 0.5D;
	        this.motZ *= 0.5D;
	        this.motY -= 0.02D;
	        if ((this.positionChanged) && (c(this.motX, this.motY + 0.6000000238418579D - this.locY + d0, this.motZ))) {
	          this.motY = 0.30000001192092896D;
	        }
	      }
	      else
	      {
	        float f5 = 0.91F;
	        if (this.onGround) {
	          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        }
	        float f6 = 0.16277136F / (f5 * f5 * f5);
	        float f3;
	        if (this.onGround) {
	          f3 = this.bm* f6;
	        } else {
	          f3 = this.aM;
	        }
	        a(f, f1, f3);
	        f5 = 0.91F;
	        if (this.onGround) {
	          f5 = this.world.getType(new BlockPosition(MathHelper.floor(this.locX), MathHelper.floor(getBoundingBox().b) - 1, MathHelper.floor(this.locZ))).getBlock().frictionFactor * 0.91F;
	        }
	        if (k_())
	        {
	          float f4 = 0.15F;
	          this.motX = MathHelper.a(this.motX, -f4, f4);
	          this.motZ = MathHelper.a(this.motZ, -f4, f4);
	          this.fallDistance = 0.0F;
	          if (this.motY < -0.15D) {
	            this.motY = -0.15D;
	          }
	        }
	        move(this.motX, this.motY, this.motZ);
	        if ((this.positionChanged) && (k_())) {
	          this.motY = 0.2D;
	        }
	        if ((this.world.isClientSide) && ((!this.world.isLoaded(new BlockPosition((int)this.locX, 0, (int)this.locZ))) || (!this.world.getChunkAtWorldCoords(new BlockPosition((int)this.locX, 0, (int)this.locZ)).o())))
	        {
	          if (this.locY > 0.0D) {
	            this.motY = -0.1D;
	          } else {
	            this.motY = 0.0D;
	          }
	        }
	        else {
	          this.motY -= 0.08D;
	        }
	        this.motY *= 0.9800000190734863D;
	        this.motX *= f5;
	        this.motZ *= f5;
	    }
	    this.aA = this.aB;
	    double d0 = this.locX - this.lastX;
	    double d1 = this.locZ - this.lastZ;
	    
	    float f2 = MathHelper.sqrt(d0 * d0 + d1 * d1) * 4.0F;
	    if (f2 > 1.0F) {
	      f2 = 1.0F;
	    }
	    this.aB += (f2 - this.aB) * 0.4F;
	    this.aC += this.aB;
	}
	
	protected void bF()
	  {
	    this.motY = 0.42f;
	    if (isSprinting())
	    {
	      float f = this.yaw * 0.017453292F;
	      
	      this.motX -= MathHelper.sin(f) * 0.2F;
	      this.motZ += MathHelper.cos(f) * 0.2F;
	    }
	    this.ai = true;
	  }
	  
	  protected void bG()
	  {
	    this.motY += 0.03999999910593033D;
	  }
	  
	  protected void bH()
	  {
	    this.motY += 0.03999999910593033D;
	  }
	  
	  public boolean k_()
	  {
	    int i = MathHelper.floor(this.locX);
	    int j = MathHelper.floor(getBoundingBox().b);
	    int k = MathHelper.floor(this.locZ);
	    Block block = this.world.getType(new BlockPosition(i, j, k)).getBlock();
	    
	    return ((block == Blocks.LADDER) || (block == Blocks.VINE));
	  }
	
	protected void a(double d0, boolean flag, Block block, BlockPosition blockposition) { }
	
	@Override
	public float getClimbHeight()
	{
		return this.climbHeight;
	}

	@Override
	public void setClimbHeight(float climbHeight)
	{
		this.climbHeight = climbHeight;
	}

	@Override
	public float getJumpHeight()
	{
		return this.jumpHeight;
	}

	@Override
	public void setJumpHeight(float jumpHeight)
	{
		this.jumpHeight = jumpHeight;
	}
	
	@Override
	public float getJumpThrust()
	{
		return this.jumpThrust;
	}

	@Override
	public void setJumpThrust(float jumpThrust)
	{
		this.jumpThrust = jumpThrust;
	}

	@Override
	public float getSpeed()
	{
		return this.speed;
	}

	@Override
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	@Override
	public float getBackwardSpeed()
	{
		return this.backwardSpeed;
	}

	@Override
	public void setBackwardSpeed(float backwardSpeed)
	{
		this.backwardSpeed = backwardSpeed;
	}

	@Override
	public float getSidewaySpeed()
	{
		return this.sidewaySpeed;
	}

	@Override
	public void setSidewaySpeed(float sidewaySpeed)
	{
		this.sidewaySpeed = sidewaySpeed;
	}
}
