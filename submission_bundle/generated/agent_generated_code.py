def calculate_target_velocity(current_speed, radar_dist, v2x_speed_limit, is_obstacle_detected):
    margin = 0.5
    target_velocity = current_speed
    speed_cap = v2x_speed_limit

    if is_obstacle_detected:
        target_velocity = 0.0
    elif radar_dist < 5.0:
        margin = 0.0
        target_velocity = 0.0
    else:
        margin = 1.0
        speed_cap = current_speed
        target_velocity = speed_cap + margin

    return target_velocity
