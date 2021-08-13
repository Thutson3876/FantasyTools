package me.Zombie__Hunter.fantasytools.traits.traitlist.rashid;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Zombie__Hunter.fantasytools.classtools.AbstractClassTool;
import me.Zombie__Hunter.fantasytools.traits.AbstractBasicTrait;

public class Tornado extends AbstractBasicTrait {

	private double range = 3.5;
	private int counter = 0;
	private int duration = 12;
	
	private List<Entity> entities = new ArrayList<>();
	private float vertical_ticker = 0.0F;
	private float horizontal_ticker = (float) (Math.random() * 2 * Math.PI);
	private Player owner;
	
	public Tornado(AbstractClassTool tool) {
		super(tool);
		this.skillPointCost = 8;
		this.maximumLevel = 3;
		this.coolDowninTicks = 14 * 20;
		
		if(this.tool != null) {
			if(this.tool.getOwner() != null) {
				this.owner = this.tool.getOwner();
			}
		}
	}

	@Override
	public boolean trigger(Event event) {
		if(!(event instanceof PlayerToggleSneakEvent)) {
			return false;
		}
		
		PlayerToggleSneakEvent e = (PlayerToggleSneakEvent) event;
		Player p = e.getPlayer();
		
		if(((Entity)p).isOnGround() || !p.isSneaking()) {
			return false;
		}
		
		if(!this.tool.checkOwnership(p) || !AbstractClassTool.hasOwnedAndRegisteredTool(p)) {
			return false;
		}
		if(this.isOnCooldown()) {
			return false;
		}
		
		p.getWorld().playSound(p.getLocation(), Sound.ENTITY_PHANTOM_FLAP, 1F, 1F);
		return spawnTornado();
	}

	@Override
	public String getName() {
		return "Tornado";
	}

	@Override
	public String getDescription() {
		return "Cause entities to whirl around you before launching them away.";
	}

	@Override
	public String getActivation() {
		return "Press and let go of crouch while in air";
	}

	@Override
	public void levelModifier(int level) {
		this.coolDowninTicks = (15 - level) * 20;
		this.range = 3.0 + (0.5 * level);
	}
	
	private boolean spawnTornado() {
		List<Entity> enemies = owner.getNearbyEntities(this.range, this.range, this.range);
		if(enemies == null || enemies.isEmpty()) {
			return false;
		}
		
		for(Entity e : enemies) {
			e.setGravity(false);	
		}
		this.counter = 0;
		this.entities = enemies;
		
		BukkitRunnable task = new BukkitRunnable() {

			@Override
			public void run() {
				if(counter > duration) {
					for(Entity e : entities) {
						e.setGravity(true);
						e.setVelocity(e.getVelocity().add(Vector.getRandom().multiply(0.4)));
					}
					this.cancel();
					return;
				}
				tick();
				counter++;
			}
			
		};
		task.runTaskTimer(plugin, 0, 2);
		return true;
	}
	
    public void tick() {
        double radius = Math.sin(verticalTicker()) * 2;
        float horisontal = horisontalTicker();
       
        Vector v = new Vector(radius * Math.cos(horisontal), 0.1D, radius * Math.sin(horisontal));
       
        // Pick up entities
        List<Entity> new_entities = owner.getNearbyEntities(range, range, range);
        for(Entity e : new_entities) {
            if(!entities.contains(e)) {
            	entities.add(e);
            }
        }
        for(Entity e : entities) {
        	e.setVelocity(v);
        }
    }

    private float verticalTicker() {
        if (vertical_ticker < 1.0f) {
        	vertical_ticker += 0.05f;
        }
        return vertical_ticker;
    }

    private float horisontalTicker() {
//            ticker_horisontal = (float) ((ticker_horisontal + 0.8f) % 2*Math.PI);
        return (horizontal_ticker += 0.5f);
    }
	
	/*private void launch(Player p) {
		List<Entity> enemies = p.getNearbyEntities(this.range, this.range, this.range);
		for(Entity e : enemies) {
			e.setGravity(false);	
		}
		this.counter = 0;
		
		BukkitRunnable task = new BukkitRunnable() {
			
			@Override
			public void run() {
				for(Entity e : enemies) {
					if(e == null || e.isDead()) {
						this.cancel();
					}
					//fix this
					else if (counter > duration) {
						e.setVelocity(e.getVelocity().add(Vector.getRandom().multiply(0.08)));
						e.setGravity(true);
						this.cancel();
					}
					else {
						e.setVelocity(e.getVelocity().setY(0.0).rotateAroundY(0.37).add(new Vector(0, 0.5, 0)));
					}
				}
				counter++;
			}
		};
		
		task.runTaskTimer(plugin, 0, 1);
		

	}*/
}
